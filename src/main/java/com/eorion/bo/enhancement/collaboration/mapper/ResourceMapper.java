package com.eorion.bo.enhancement.collaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CooperationResourceTypeStatisticsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    List<ResourceDetailDTO> selectForList(
            @Param("projectId") Integer projectId,
            @Param("nameLike") String nameLike,
            @Param("parentNode") Integer parentNode,
            @Param("status") String status,
            @Param("tags") List<String> tags,
            @Param("typesIn") List<String> typesIn
            );

    List<ResourceDetailDTO> searchForList(
            @Param("projectId") Integer projectId,
            @Param("nameLike") String nameLike,
            @Param("parentNode") Integer parentNode,
            @Param("status") String status,
            @Param("tags") List<String> tags,
            @Param("typesIn") List<String> typesIn,
            @Param("projectTypesIn") List<String> projectTypesIn
    );

    List<CooperationResourceTypeStatisticsDTO> selectStatistics(
            @Param("projectId") Integer projectId,
            @Param("nameLike") String nameLike,
            @Param("parentNode") Integer parentNode,
            @Param("status") String status,
            @Param("tags") List<String> tags,
            @Param("typesIn") List<String> typesIn
    );
}
