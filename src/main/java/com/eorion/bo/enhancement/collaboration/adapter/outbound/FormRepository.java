package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormListDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import com.eorion.bo.enhancement.collaboration.mapper.CollaborationFormMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FormRepository extends ServiceImpl<CollaborationFormMapper, CollaborationForm> {


    private final CollaborationFormMapper collaborationFormMapper;

    public FormRepository(CollaborationFormMapper collaborationFormMapper) {
        this.collaborationFormMapper = collaborationFormMapper;
    }

    public List<FormListDTO> getFromListPage(String nameLike, String definitionKey, String tenant, String createdBy,
                                             String type, String sort,
                                             Integer firstResult, Integer maxResults) {
        return collaborationFormMapper.getFromListByPage(nameLike, definitionKey, tenant, createdBy, type, sort, firstResult, maxResults);
    }
}
