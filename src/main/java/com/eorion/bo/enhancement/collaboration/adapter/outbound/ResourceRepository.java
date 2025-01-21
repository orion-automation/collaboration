package com.eorion.bo.enhancement.collaboration.adapter.outbound;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CooperationResourceTypeStatisticsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.mapper.ResourceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResourceRepository extends ServiceImpl<ResourceMapper, Resource> {
    private final ResourceMapper mapper;

    public List<ResourceDetailDTO> selectForList(Integer projectId, String nameLike, Integer parentNode, String status, List<String> tags, List<String> typesIn) {
        return mapper.selectForList(projectId, nameLike, parentNode, status, tags, typesIn);
    }

    public List<ResourceDetailDTO> searchForList(Integer projectId, String nameLike, Integer parentNode, String status, List<String> tags, List<String> typesIn, List<String> projectTypesIn) {
        return mapper.searchForList(projectId, nameLike, parentNode, status, tags, typesIn, projectTypesIn);
    }

    public List<CooperationResourceTypeStatisticsDTO> selectStatistics(Integer projectId, String nameLike, Integer parentNode, String status, List<String> tags, List<String> typesIn) {
        return mapper.selectStatistics(projectId, nameLike, parentNode, status, tags, typesIn);
    }
}
