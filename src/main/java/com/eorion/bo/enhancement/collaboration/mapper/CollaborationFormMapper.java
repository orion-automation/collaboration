package com.eorion.bo.enhancement.collaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormListDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollaborationFormMapper extends BaseMapper<CollaborationForm> {

    List<FormListDTO> getFromListByPage(@Param("nameLike") String nameLike, @Param("definitionKey") String definitionKey,
                                        @Param("tenant") String tenant, @Param("createdBy") String createdBy,
                                        @Param("type") String type, @Param("sort") String sort,
                                        @Param("firstResult") Integer firstResult, @Param("maxResults") Integer maxResults);
}