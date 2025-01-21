package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRepository extends ServiceImpl<ProjectMapper, Project> {
    private final ProjectMapper mapper;

    public List<Project> getProjectList(String tenant, String name, String type, String codeCode, List<String> tags){
        return mapper.queryForList(tenant, name, type, codeCode, tags);
    }
}
