package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import com.eorion.bo.enhancement.collaboration.mapper.AssociationMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AssociationRepository extends ServiceImpl<AssociationMapper, Association> {
}
