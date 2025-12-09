package com.eorion.bo.enhancement.collaboration.adapter.inbound.form;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.form.FormUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOString;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CountDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.FormListDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.service.CollaborationFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/form")
@Tag(name = "Collaboration Management - Form")
public class CollaborationFormController {

    private final CollaborationFormService collaborationFormService;

    public CollaborationFormController(CollaborationFormService collaborationFormService) {
        this.collaborationFormService = collaborationFormService;
    }

    /**
     * 创建form
     */
    @Operation(summary = "Create form information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTOString.class))),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Error when creating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    public IdDTO<String> createForm(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Form information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FormSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                    "name": "test-5",
                                    "createdBy": "demo",
                                    "definitionKey": "definition-key",
                                    "type": "form",
                                    "tenant": "tenant",
                                    "formData": []
                                    }""")))
            @Valid @RequestBody FormSaveDTO saveDTO) throws InsertFailedException {
        return collaborationFormService.createForm(saveDTO);
    }

    /**
     * 修改form
     */
    @Operation(summary = "Update a form by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Data with given id not exist", content = @Content),
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFormById(
            @Parameter(
                    description = "form id", required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Form information to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FormUpdateDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                    "name": "test-5",
                                    "updatedBy": "demo",
                                    "definitionKey": "definition-key",
                                    "type": "form",
                                    "formData": []
                                    }""")))
            @Valid @RequestBody FormUpdateDTO updateDTO) throws DataNotExistException {
        collaborationFormService.updateFormById(id, updateDTO);
    }

    /**
     * 删除form
     */
    @Operation(summary = "Delete a form by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Data with given id not exist", content = @Content),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFormById(
            @Parameter(
                    description = "form id",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @PathVariable String id) throws DataNotExistException {
        collaborationFormService.deleteFormById(id);
    }

    /**
     * 根据formId获取detail
     */
    @Operation(summary = "Get a form by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FormDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Data with given id not exist", content = @Content),
    })
    @GetMapping("/{id}")
    public FormDetailDTO getFormById(
            @Parameter(
                    description = "form id",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @PathVariable String id) throws DataNotExistException {
        return collaborationFormService.getFormById(id);
    }

    @Operation(summary = "Get a form by definition key")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FormDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Data with given id not exist", content = @Content),
    })
    @GetMapping("/definition-key/{key}")
    public FormDetailDTO getFormByDefinitionKey(
            @Parameter(
                    description = "definition key",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @PathVariable String key) throws DataNotExistException {
        return collaborationFormService.getByDefinitionKey(key);
    }

    /**
     * 统计form数量
     */

    @Operation(summary = "Query count of form information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountDTO.class))),
            @ApiResponse(responseCode = "400", description = "One or more query parameters format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/count")
    public CountDTO getFromCount(
            @Parameter(
                    description = "Optional definition key id condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "definitionKey", required = false) String definitionKey,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional created by user id condition",
                    schema = @Schema(type = "string", maxLength = 20)
            )
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(
                    description = "Optional type condition",
                    schema = @Schema(type = "string", maxLength = 16)
            )
            @RequestParam(value = "type", required = false) String type,
            @Parameter(
                    description = "Optional tenant condition",
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "tenant", required = false) String tenant
    ) {
        return collaborationFormService.getFormCount(nameLike, definitionKey, createdBy, type, tenant);
    }

    /**
     * form 列表查询
     */
    @Operation(summary = "Query list of form information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FormDetailDTO.class))
                    )),
    })
    @GetMapping
    public List<FormListDTO> queryFormList(
            @Parameter(
                    description = "Optional definition key id condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "definitionKey", required = false) String definitionKey,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional created by user id condition",
                    schema = @Schema(type = "string", maxLength = 20)
            )
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(
                    description = "Optional type condition",
                    schema = @Schema(type = "string", maxLength = 16)
            )
            @RequestParam(value = "type", required = false) String type,
            @Parameter(
                    description = "Optional tenant condition",
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "tenant", required = false) String tenant,
            @Parameter(
                    description = "Order by field",
                    schema = @Schema(type = "string")
            )
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @Parameter(
                    description = "Sort condition",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @Parameter(
                    description = "Zero-based page index (0..N)",
                    example = "0",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "0")
            )
            @RequestParam(value = "firstResult", required = false, defaultValue = "0") Integer firstResult,
            @Parameter(
                    description = "The size of the page to be returned",
                    example = "20",
                    schema = @Schema(type = "integer", format = "int32", defaultValue = "2147483647")
            )
            @RequestParam(value = "maxResults", required = false, defaultValue = "20") Integer maxResults
    ) {
        return collaborationFormService.getFromList(nameLike, definitionKey, tenant, createdBy, type, sortBy, sortOrder, firstResult, maxResults);
    }

}
