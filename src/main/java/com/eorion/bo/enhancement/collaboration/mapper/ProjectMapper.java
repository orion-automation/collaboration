package com.eorion.bo.enhancement.collaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    List<Project>  queryForList(@Param("tenant") String tenant, @Param("nameLike") String nameLike,
                                @Param("type") String type, @Param("coeCode") String coeCode,
                                @Param("tags") List<String> tags);
}
