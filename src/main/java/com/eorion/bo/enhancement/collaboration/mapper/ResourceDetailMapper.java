package com.eorion.bo.enhancement.collaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceDetailMapper extends BaseMapper<ResourceDetail> {

    Integer findMaxVersionByResourceId(@Param("resourceId") Long resourceId);

}
