package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.MemberRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.MemberStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.ProjectStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CodeEffortDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectListDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository repository;
    private final MemberRepository memberRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceDetailRepository resourceDetailRepository;
    private final IdentityService identityService;
    private final ProjectStructureMapper projectStructureMapper;
    private final MemberStructureMapper memberStructureMapper;

    public ProjectService(ProjectRepository repository, MemberRepository memberRepository,
                          ResourceRepository resourceRepository, ResourceDetailRepository resourceDetailRepository,
                          IdentityService identityService, ProjectStructureMapper projectStructureMapper,
                          MemberStructureMapper memberStructureMapper) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.resourceRepository = resourceRepository;
        this.resourceDetailRepository = resourceDetailRepository;
        this.identityService = identityService;
        this.projectStructureMapper = projectStructureMapper;
        this.memberStructureMapper = memberStructureMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public IdDTO<?> saveProject(ProjectSaveDTO saveDTO) throws InsertFailedException {
        var project = projectStructureMapper.saveDtoToEntity(saveDTO);
        var userId = identityService.getCurrentAuthentication().getUserId();
        project.setOwner(userId);
        project.setConfigJson(saveDTO.getConfigJson());
        if (!repository.save(project)) {
            throw new InsertFailedException("project保存失败！");
        }
        Member member = new Member();
        member.setUserId(userId);
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.OWNER);
        member.setName(userId);
        if (!memberRepository.save(member)) {
            throw new InsertFailedException("member保存数据失败！");
        }
        return new IdDTO<>(project.getId());
    }

    public void deleteProjectById(Integer id) throws UpdateFailedException {
        var userId = identityService.getCurrentAuthentication().getUserId();
        var project = repository.getById(id);
        if (Objects.nonNull(project) && project.getOwner().equals(userId)) {
            repository.removeById(id);
        } else {
            throw new UpdateFailedException("更新数据错误！");
        }

    }

    public void updateProject(Integer id, ProjectUpdateDTO updateDto) throws UpdateFailedException {
        var projectDB = repository.getById(id);
        if (Objects.isNull(projectDB)) {
            throw new RuntimeException();
        }
        var project = projectStructureMapper.updateDtoToEntity(updateDto);
        project.setId(id);
        if (StringUtils.hasLength(updateDto.getConfigJson()))
            project.setConfigJson(updateDto.getConfigJson());
        if (!repository.updateById(project)) {
            throw new UpdateFailedException("更新数据错误！");
        }
    }

    public List<ProjectListDTO> queryProject(String tenant, String nameLike, String coeCode, String type, List<String> tags) {
        var projects = repository.getProjectList(tenant, nameLike, type, coeCode, tags);
        var destList = projects.stream().map(projectStructureMapper::toDto).collect(Collectors.toList());
        destList.forEach(item -> {
            var members = memberRepository.list(new LambdaQueryWrapper<Member>()
                    .eq(Member::getProjectId, item.getId())
            );

            item.setMembers(members.stream().map(memberStructureMapper::toMemberDetailDTO).collect(Collectors.toList()));
        });
        return destList;
    }

    public ProjectDetailDTO getProjectById(Integer id) throws DataNotExistException {
        var project = repository.getById(id);
        if (Objects.isNull(project)) {
            throw new DataNotExistException("该ID对应的数据不存在，请检查对应ID");
        }
        return ProjectDetailDTO.fromEntity(project);
    }

    public CodeEffortDTO getProcessCodeEffort(Integer projectId) {
        var resourceList = resourceRepository.list(
                        new LambdaQueryWrapper<Resource>().eq(Resource::getProjectId, projectId)
                )
                .stream()
                .map(Resource::getId)
                .collect(Collectors.toList());
        var resourceDetails = resourceDetailRepository.list(new LambdaQueryWrapper<ResourceDetail>().in(ResourceDetail::getResourceId, resourceList));
        long codeEffort = 0L;
        for (ResourceDetail resourceDetail : resourceDetails) {
            long item = resourceDetail.getZeroCodeEffort() + resourceDetail.getLowCodeEffort() + resourceDetail.getAdvanceCodeEffort();
            codeEffort += item;
        }
        return new CodeEffortDTO(codeEffort);
    }
}
