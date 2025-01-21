package com.eorion.bo.enhancement.collaboration.adapter.inbound.form;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CountDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormListDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.service.CollaborationFormService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/form")
@AllArgsConstructor
public class CollaborationFormController {

    private final CollaborationFormService collaborationFormService;

    /**
     * 创建form
     */
    @PostMapping("")
    public IdDTO<String> createForm(@RequestBody FormSaveDTO saveDTO) throws InsertFailedException {
        return collaborationFormService.createForm(saveDTO);
    }

    /**
     * 修改form
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFormById(@PathVariable String id, @RequestBody FormUpdateDTO updateDTO) throws DataNotExistException {
        collaborationFormService.updateFormById(id, updateDTO);
    }

    /**
     * 删除form
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFormById(@PathVariable String id) throws DataNotExistException {
        collaborationFormService.deleteFormById(id);
    }

    /**
     * 根据formId获取detail
     */
    @GetMapping("/{id}")
    public FormDetailDTO getFormById(@PathVariable String id) throws DataNotExistException {
        return collaborationFormService.getFormById(id);
    }

    @GetMapping("/definition-key/{key}")
    public FormDetailDTO getFormByDefinitionKey(@PathVariable String key) throws DataNotExistException {
        return collaborationFormService.getByDefinitionKey(key);
    }

    /**
     * 统计form数量
     */

    @GetMapping("/count")
    public CountDTO getFromCount(
            @RequestParam(value = "definitionKey", required = false) String definitionKey,
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "tenant", required = false) String tenant
    ) {
        return collaborationFormService.getFormCount(nameLike, definitionKey, createdBy, type, tenant);
    }

    /**
     * form 列表查询
     */
    @GetMapping("")
    public List<FormListDTO> queryFormList(

            @RequestParam(value = "definitionKey", required = false) String definitionKey,
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @RequestParam(value = "tenant", required = false) String tenant,
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @RequestParam(value = "maxResults", required = false, defaultValue = "20") Integer maxResults
    ) {
        return collaborationFormService.getFromList(nameLike, definitionKey, tenant, createdBy, type, sortBy, sortOrder, firstResult, maxResults);
    }

}
