package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.MemberRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberDeleteDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    public MemberService(MemberRepository memberRepository, ProjectRepository projectRepository, IdentityService identityService) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.identityService = identityService;
    }

    private final IdentityService identityService;

    public IdDTO<Integer> saveOrUpdate(Member member) throws InsertFailedException {
        if (Objects.nonNull(projectRepository.getById(member.getProjectId()))) {
            var memberResult = memberRepository.getOne(new LambdaQueryWrapper<Member>()
                    .eq(Member::getProjectId, member.getProjectId())
                    .eq(Member::getUserId, member.getUserId()));
            if (Objects.nonNull(memberResult)) {
                member.setId(memberResult.getId());
                memberRepository.updateById(member);
            } else {
                memberRepository.save(member);
            }
            return new IdDTO<>(member.getId());
        } else {
            throw new InsertFailedException("请检查对应的项目ID是否存在");
        }

    }

    public void deleteMember(MemberDeleteDTO deleteMember) {
        var userId = identityService.getCurrentAuthentication().getUserId();
        var project = projectRepository.getOne(new LambdaQueryWrapper<Project>().eq(Project::getId, deleteMember.getProjectId()));
        if (Objects.nonNull(project) && project.getOwner().equals(userId)) {
            memberRepository.remove(new LambdaQueryWrapper<Member>()
                    .eq(Member::getProjectId, deleteMember.getProjectId())
                    .eq(Member::getUserId, deleteMember.getUserId())
            );
        } else {
            throw new RuntimeException("请检查您是否为该项目的owner，或者该对应项目是否存在");
        }
    }

    public List<Member> queryMemberList(Integer projectId) throws DataNotExistException {
        var project = projectRepository.getById(projectId);
        if (Objects.isNull(project)) {
            throw new DataNotExistException("该ID对应的数据不存在，请检查对应ID");
        }
        return memberRepository.list(new LambdaQueryWrapper<Member>().eq(Member::getProjectId, projectId));
    }
}
