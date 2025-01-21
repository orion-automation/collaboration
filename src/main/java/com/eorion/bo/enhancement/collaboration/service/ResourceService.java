package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceUpdatedDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.CoopResourceTagsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.ResourceStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.*;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.domain.enums.CoopResourceStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.IllegalParameterException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository repository;
    private final ResourceDetailRepository detailRepository;
    private final ProjectRepository projectRepository;
    private final ResourceStructureMapper structureMapper;

    public ResourceService(ResourceRepository repository, ResourceDetailRepository detailRepository,
                           ProjectRepository projectRepository, ResourceStructureMapper structureMapper) {
        this.repository = repository;
        this.detailRepository = detailRepository;
        this.projectRepository = projectRepository;
        this.structureMapper = structureMapper;
    }

    public IdDTO<Integer> saveResource(ResourceSaveDTO saveDTO) throws InsertFailedException {
        Resource resource = structureMapper.saveDtoToEntity(saveDTO);
        resource.setConfigJson(saveDTO.getConfigJson());
        if (!repository.save(resource)) {
            throw new InsertFailedException("插入数据失败！");
        }
        return new IdDTO<>(resource.getId());
    }

    public void updatedResource(Integer resourceId, ResourceUpdatedDTO updatedDTO) throws UpdateFailedException, DataNotExistException {
        var projectDB = repository.getById(resourceId);
        if (Objects.isNull(projectDB)) {
            throw new DataNotExistException("请检查对应资源是否存在！");
        }
        Resource resource = structureMapper.updateDtoToEntity(updatedDTO);
        resource.setId(resourceId);
        if (StringUtils.isNotBlank(updatedDTO.getConfigJson())) {
            resource.setConfigJson(updatedDTO.getConfigJson());
        }
        if (!repository.updateById(resource)) {
            throw new UpdateFailedException("更新数据错误！");
        }
    }

    public void deleteResource(Integer resourceId) throws DataNotExistException {
        if (Objects.isNull(repository.getById(resourceId))) {
            throw new DataNotExistException("请检查对应资源是否存在！");
        }
        repository.removeById(resourceId);
    }

    public List<ResourceDetailDTO> getResourceList(Integer projectId, String nameLike, Integer parentNode, String status, String tags, String type) throws IllegalParameterException {

        CoopResourceStatus resStatus;
        if (StringUtils.isNotEmpty(status)) {
            resStatus = CoopResourceStatus.from(status);
            if (Objects.isNull(resStatus)) {
                throw new IllegalParameterException(String.format("status [%s] is not a valid argument !", status));
            }
        }
        List<String> tagsList = null;
        if (StringUtils.isNotEmpty(tags))
            tagsList = List.of(tags.split(","));

        if (StringUtils.isNotEmpty(type)) {
            if (Objects.nonNull(ResourceType.from(type))) {
                return repository.selectForList(projectId, nameLike, parentNode, status, tagsList, List.of(type));
            } else {
                throw new IllegalParameterException(String.format("type [%s] is not a valid argument !", type));
            }
        }
        return repository.selectForList(projectId, nameLike, parentNode, status, tagsList, null);
    }

    public CooperationResourceDetailDTO getResourceById(Integer resourceId) throws DataNotExistException {
        var resource = repository.getById(resourceId);
        if (Objects.isNull(resource)) {
            throw new DataNotExistException("请检查对应资源是否存在！");
        }

        return CooperationResourceDetailDTO.fromEntity(resource);
    }

    public ResourceBriefDTO getResourceBrieById(Integer resourceId) throws DataNotExistException {
        var resource = repository.getById(resourceId);
        if (Objects.isNull(resource)) {
            throw new DataNotExistException("请检查对应资源是否存在！");
        }
        ResourceBriefDTO resourceBrief = new ResourceBriefDTO();
        resourceBrief.setName(resource.getName());
        resourceBrief.setId(resource.getId());
        resourceBrief.setResourceType(resource.getType());

        Integer projectId = resource.getProjectId();
        if (Objects.nonNull(projectId)) {
            var project = projectRepository.getById(projectId);
            resourceBrief.setProjectId(projectId);
            resourceBrief.setProjectType(project.getType());
        }
        return resourceBrief;
    }

    public CodeEffortDTO getProcessCodeEffort(Integer resourceId) {

        var list = detailRepository.list(new LambdaQueryWrapper<ResourceDetail>().eq(ResourceDetail::getResourceId, resourceId));
        long codeEffort = 0L;
        for (ResourceDetail resourceDetail : list) {
            long item = resourceDetail.getZeroCodeEffort() + resourceDetail.getLowCodeEffort() + resourceDetail.getAdvanceCodeEffort();
            codeEffort += item;
        }
        return new CodeEffortDTO(codeEffort);
    }

    public void coopResourceTakeDown(Long resourceId) throws UpdateFailedException, DataNotExistException {

        var dbResource = repository.getById(resourceId);
        if (Objects.isNull(dbResource)) {
            throw new DataNotExistException("对应ID信息不存在！");
        }
        dbResource.setStatus(CoopResourceStatus.DRAFT);
        if (!repository.updateById(dbResource)) {
            throw new UpdateFailedException("更新失败");
        }
    }

    public void coopResourcePublish(Long resourceId) throws DataNotExistException, UpdateFailedException {
        var dbResource = repository.getById(resourceId);
        if (Objects.isNull(dbResource)) {
            throw new DataNotExistException("对应ID信息不存在！");
        }
        dbResource.setStatus(CoopResourceStatus.PUBLISH);
        if (!repository.updateById(dbResource)) {
            throw new UpdateFailedException("更新失败");
        }

    }

    public void coopResourceTagsUpdate(Long resourceId, CoopResourceTagsDTO tags) throws DataNotExistException, IllegalParameterException {
        var dbResource = repository.getById(resourceId);
        if (Objects.isNull(dbResource))
            throw new DataNotExistException("对应ID信息不存在！");
        if (tags != null && tags.getTags() != null) {
            var updateWrapper = new LambdaUpdateWrapper<Resource>();
            updateWrapper.eq(Resource::getId, dbResource.getId());
            updateWrapper.set(Resource::getTags, tags.getTags());
            repository.update(updateWrapper);
        } else {
            throw new IllegalParameterException("请检查传入参数是否正确！");
        }
    }

    public List<ResourceDetailDTO> selectResourceList(Integer projectId, String nameLike, Integer parentNode,
                                                      String status, String tags, String typesIn, String projectTypesIn) throws IllegalParameterException {

        CoopResourceStatus resStatus;
        if (StringUtils.isNotEmpty(status)) {
            resStatus = CoopResourceStatus.from(status);
            if (Objects.isNull(resStatus)) {
                throw new IllegalParameterException(String.format("status [%s] is not a valid argument !", status));
            }
        }
        List<String> tagsList = null;
        if (StringUtils.isNotEmpty(tags))
            tagsList = List.of(tags.split(","));

        List<String> projectTypeList = null;
        if (StringUtils.isNotEmpty(projectTypesIn)) {
            projectTypeList = Arrays.stream(projectTypesIn.split(","))
                    .map(ProjectType::from)
                    .filter(Objects::nonNull)
                    .map(ProjectType::getValue)
                    .collect(Collectors.toList());
        }

        List<String> typeList = null;
        if (StringUtils.isNotEmpty(typesIn)) {
            typeList = Arrays.stream(typesIn.split(","))
                    .map(ResourceType::from)
                    .filter(Objects::nonNull)
                    .map(ResourceType::getValue)
                    .collect(Collectors.toList());
        }

        return repository.searchForList(projectId, nameLike, parentNode, status, tagsList, typeList, projectTypeList);
    }

    public List<CooperationResourceTypeStatisticsDTO> getResourceStatistics(Integer projectId, String nameLike, Integer parentNode, String status, String tags, String type) throws IllegalParameterException {

        CoopResourceStatus resStatus;
        if (StringUtils.isNotEmpty(status)) {
            resStatus = CoopResourceStatus.from(status);
            if (Objects.isNull(resStatus)) {
                throw new IllegalParameterException(String.format("status [%s] is not a valid argument !", status));
            }
        }
        List<String> tagsList = null;
        if (StringUtils.isNotEmpty(tags))
            tagsList = List.of(tags.split(","));

        if (StringUtils.isNotEmpty(type)) {
            if (Objects.nonNull(ResourceType.from(type))) {
                return repository.selectStatistics(projectId, nameLike, parentNode, status, tagsList, List.of(type));
            } else {
                throw new IllegalParameterException(String.format("type [%s] is not a valid argument !", type));
            }
        }
        return repository.selectStatistics(projectId, nameLike, parentNode, status, tagsList, null);
    }
}
