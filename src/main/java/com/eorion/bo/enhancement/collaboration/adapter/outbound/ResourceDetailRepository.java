package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.mapper.ResourceDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceDetailRepository extends ServiceImpl<ResourceDetailMapper, ResourceDetail> {

    private final ResourceDetailMapper mapper;

    public Integer getMaxVersion(Integer resourceId) {
        return mapper.findMaxVersionByResourceId(resourceId);
    }
}
