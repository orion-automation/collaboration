package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.ApplicationResource;
import com.eorion.bo.enhancement.collaboration.mapper.ApplicationResourceMapper;
import org.springframework.stereotype.Service;

@Service
public class ApplicationResourceRepository extends ServiceImpl<ApplicationResourceMapper, ApplicationResource> {
}
