package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceUpdatedDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.CoopResourceTagsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOInteger;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CooperationResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CooperationResourceTypeStatisticsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.IllegalParameterException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import com.eorion.bo.enhancement.collaboration.service.ResourceService;
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
@RequestMapping("/enhancement/collaboration/resource")
@Tag(name = "Collaboration Management - Resource")
public class CooperationResourceController {

    private final ResourceService resourceService;

    public CooperationResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Operation(summary = "Create resource information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTOInteger.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Error when inserting",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    public IdDTO<?> saveResource(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Resource information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "projectId": 1,
                                        "name": "test.bpmn",
                                        "type": "3",
                                        "parentNode": 1,
                                        "externalResourceId": "www.baidu.com",
                                        "tags": "tags,cat",
                                        "configJson": {"key": "value"}
                                    }""")))
            @Valid @RequestBody ResourceSaveDTO resource) throws InsertFailedException {
        return resourceService.saveResource(resource);
    }

    @Operation(summary = "Query list of resources' detail information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResourceDetailDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "One or more request parameters format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/list")
    public List<ResourceDetailDTO> getResourceList(
            @Parameter(
                    description = "Project id condition",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "int", format = "int32", minimum = "0")
            )
            @RequestParam(value = "projectId") int projectId,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 50)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional parent node condition",
                    schema = @Schema(type = "integer", format = "int32")
            )
            @RequestParam(value = "parentNode", required = false) Integer parentNode,
            @Parameter(
                    description = "Optional status condition",
                    schema = @Schema(type = "string", allowableValues = {"0", "1", "2"})
            )
            @RequestParam(value = "status", required = false) String status,
            @Parameter(
                    description = "Optional tags condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "tags", required = false) String tags,
            @Parameter(
                    description = "Optional type condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "type", required = false) String type
    ) throws IllegalParameterException {
        return resourceService.getResourceList(projectId, nameLike, parentNode, status, tags, type);
    }

    @Operation(summary = "Query list of resources' detail information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResourceDetailDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "One or more request parameters format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping
    public List<ResourceDetailDTO> selectResourceList(
            @Parameter(
                    description = "Optional project id condition",
                    example = "1",
                    schema = @Schema(type = "int", format = "int32", minimum = "0")
            )
            @RequestParam(value = "projectId", required = false) Integer projectId,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 50)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional parent node condition",
                    schema = @Schema(type = "integer", format = "int32")
            )
            @RequestParam(value = "parentNode", required = false) Integer parentNode,
            @Parameter(
                    description = "Optional status condition",
                    schema = @Schema(type = "string", allowableValues = {"0", "1", "2"})
            )
            @RequestParam(value = "status", required = false) String status,
            @Parameter(
                    description = "Optional tags condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "tags", required = false) String tags,
            @Parameter(
                    description = "Optional comma split type condition",
                    schema = @Schema(type = "string")
            )
            @RequestParam(value = "typesIn", required = false) String typesIn,
            @Parameter(
                    description = "Optional comma split project type condition",
                    schema = @Schema(type = "string")
            )
            @RequestParam(value = "projectTypesIn", required = false) String projectTypesIn
    ) throws IllegalParameterException {
        return resourceService.selectResourceList(projectId, nameLike, parentNode, status, tags, typesIn, projectTypesIn);
    }

    @Operation(summary = "Update a resource by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error when updating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatedResource(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer resourceId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Resource information to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceUpdatedDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "projectId": 1,
                                        "name": "test.bpmn",
                                        "parentNode": 1,
                                        "externalResourceId": "www.baidu.com",
                                        "tags": "tags,cat",
                                        "configJson": {"key": "value"}
                                    }""")))
            @Valid @RequestBody ResourceUpdatedDTO updatedDto) throws UpdateFailedException, DataNotExistException {
        resourceService.updatedResource(resourceId, updatedDto);
    }

    @Operation(summary = "Delete a resource by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
    })
    @DeleteMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer resourceId) throws DataNotExistException {
        resourceService.deleteResource(resourceId);
    }

    @Operation(summary = "Get a resource by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CooperationResourceDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
    })
    @GetMapping("/{resourceId}")
    public CooperationResourceDetailDTO getResourceById(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer resourceId) throws DataNotExistException {
        return resourceService.getResourceById(resourceId);
    }

    @Operation(summary = "Query statistics information list of resources")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CooperationResourceTypeStatisticsDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "One or more request parameters format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/statistics")
    public List<CooperationResourceTypeStatisticsDTO> getResourceStatistics(
            @Parameter(
                    description = "Project id condition",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "int", format = "int32", minimum = "0")
            )
            @RequestParam(value = "projectId") Integer projectId,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 50)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional parent node condition",
                    schema = @Schema(type = "integer", format = "int32")
            )
            @RequestParam(value = "parentNode", required = false) Integer parentNode,
            @Parameter(
                    description = "Optional status condition",
                    schema = @Schema(type = "string", allowableValues = {"0", "1", "2"})
            )
            @RequestParam(value = "status", required = false) String status,
            @Parameter(
                    description = "Optional tags condition",
                    schema = @Schema(type = "string", maxLength = 255)
            )
            @RequestParam(value = "tags", required = false) String tags,
            @Parameter(
                    description = "Optional type condition",
                    schema = @Schema(type = "string", allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8"})
            )
            @RequestParam(value = "type", required = false) String type) throws IllegalParameterException {
        return resourceService.getResourceStatistics(projectId, nameLike, parentNode, status, tags, type);
    }

    /**
     * 修改应用状态，0 -> 1
     */
    @Operation(summary = "Publish a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error when updating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),

    })
    @PutMapping("/{resourceId}/publish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourcePublish(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable int resourceId) throws DataNotExistException, UpdateFailedException {
        resourceService.coopResourcePublish(resourceId);
    }

    /**
     * 修改应用状态，1 -> 0
     */
    @Operation(summary = "Unpublish a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error when updating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),

    })
    @PutMapping("/{resourceId}/off")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourceTakeDown(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable int resourceId) throws DataNotExistException, UpdateFailedException {
        resourceService.coopResourceTakeDown(resourceId);
    }

    /**
     * 修改标签
     */
    @Operation(summary = "Update tags of a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),

    })
    @PutMapping("/{resourceId}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourceTagsUpdate(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable int resourceId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tags to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CoopResourceTagsDTO.class),
                            examples = @ExampleObject(value = """
                                    {"tags": "tags,cat"}""")))
            @Valid @RequestBody CoopResourceTagsDTO tags) throws DataNotExistException, IllegalParameterException {
        resourceService.coopResourceTagsUpdate(resourceId, tags);
    }
}
