package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
import com.eorion.bo.enhancement.collaboration.mapper.ResourceBpmnNodeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceBpmnNodeRepository extends ServiceImpl<ResourceBpmnNodeMapper, ResourceBpmnNode> {
}
