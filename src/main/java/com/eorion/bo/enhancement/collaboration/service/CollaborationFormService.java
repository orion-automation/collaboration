package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.FormRepository;
import com.eorion.bo.enhancement.collaboration.domain.constants.FormSortByConstant;
import com.eorion.bo.enhancement.collaboration.domain.constants.SortOrderConstant;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.FormStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CountDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormListDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollaborationFormService {
    private final FormRepository formRepository;
    private final FormStructureMapper formStructureMapper;

    public IdDTO<String> createForm(FormSaveDTO saveDTO) throws InsertFailedException {

        var destination = formStructureMapper.saveDtoToEntity(saveDTO);
        destination.setFormData(saveDTO.getFormData());

        if (!formRepository.save(destination)){
            throw new InsertFailedException("保存from失败！");
        }
        return new IdDTO<>(destination.getId());
    }

    public void updateFormById(String id, FormUpdateDTO updateDTO) throws DataNotExistException {

        var dbFrom = formRepository.getById(id);
        if (Objects.nonNull(dbFrom)){
            var form = formStructureMapper.updateDtoToEntity(updateDTO);
            form.setFormData(updateDTO.getFormData());
            form.setId(id);
            form.setDefinitionKey(updateDTO.getDefinitionKey());
            formRepository.updateById(form);
        }else {
            throw new DataNotExistException("对应表单数据不存在！");
        }
    }

    public void deleteFormById(String id) throws DataNotExistException {

        var dbForm = formRepository.getById(id);
        if (Objects.nonNull(dbForm)){
            formRepository.removeById(id);
        }else {
            throw new DataNotExistException("删除表单数据不存在");
        }

        formRepository.removeById(id);
    }

    public FormDetailDTO getFormById(String id) throws DataNotExistException {
        var form = formRepository.getById(id);
        if (Objects.isNull(form)){
            throw new DataNotExistException("对应表单不存在");
        }
        return FormDetailDTO.getData(form);
    }



    public CountDTO getFormCount(@Nullable String nameLike, @Nullable String definitionKey, @Nullable String createdBy, @Nullable String type, @Nullable String tenant) {

        LambdaQueryWrapper<CollaborationForm> countQueryWp = new LambdaQueryWrapper<>();
        countQueryWp
                .like(StringUtils.isNotEmpty(nameLike), CollaborationForm::getName, nameLike)
                .eq(StringUtils.isNotEmpty(definitionKey), CollaborationForm::getDefinitionKey, definitionKey)
                .eq(StringUtils.isNotEmpty(createdBy), CollaborationForm::getCreatedBy, createdBy)
                .eq(StringUtils.isNotEmpty(type), CollaborationForm::getType, type)
                .eq(StringUtils.isNotEmpty(tenant), CollaborationForm::getTenant, tenant);
        var count = formRepository.count(countQueryWp);
        return new CountDTO(count);
    }

    public List<FormListDTO> getFromList(String nameLike, String definitionKey, String tenant, String createdBy,
                                         String type, String sortBy, String sortOrder, Integer firstResult, Integer maxResults) {

            var sortOrderCt = Objects.isNull(SortOrderConstant.from(sortOrder)) ? "DESC" : SortOrderConstant.from(sortOrder).getValue();
            var sortByCt = Objects.isNull(FormSortByConstant.from(sortBy)) ? "CREATED_TS" : FormSortByConstant.from(sortBy).getValue();
            String sort = sortByCt + " " + sortOrderCt;
            return formRepository.getFromListPage(nameLike, definitionKey, tenant, createdBy, type, sort, firstResult, maxResults);

    }

    public FormDetailDTO getByDefinitionKey(String definitionKey) throws DataNotExistException {
        LambdaQueryWrapper<CollaborationForm> countQueryWp = new LambdaQueryWrapper<>();
        countQueryWp
                .eq(StringUtils.isNotEmpty(definitionKey), CollaborationForm::getDefinitionKey, definitionKey)
                .last("LIMIT 1");

        var result = formRepository.getOne(countQueryWp);

        if (result == null) {
            throw new DataNotExistException("对应表单不存在");
        } else {
            return FormDetailDTO.getData(result);
        }
    }
}
