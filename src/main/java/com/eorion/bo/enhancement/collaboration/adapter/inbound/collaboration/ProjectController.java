package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOInteger;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectListDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import com.eorion.bo.enhancement.collaboration.service.ProjectService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration/project")
@Tag(name = "Collaboration Management - Project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Create project information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTOInteger.class))),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Error when creating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    public IdDTO<?> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Project information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "name":"name",
                                        "tenant":"tenant",
                                        "tags":"tags",
                                        "coeCode":"code",
                                        "type":"3",
                                        "configJson": {"key": "value"}
                                    }""")))
            @Validated @RequestBody ProjectSaveDTO dto) throws InsertFailedException {
        return projectService.saveProject(dto);
    }

    @Operation(summary = "Delete a project by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Error when deleting",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable @Parameter(
                    description = "project id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            ) Integer id) throws UpdateFailedException {
        projectService.deleteProjectById(id);
    }

    @Operation(summary = "Update a project by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Error when updating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(
            @PathVariable @Parameter(
                    description = "project id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            ) Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Project information to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectUpdateDTO.class),
                            examples = @ExampleObject(value = """
                                    {"name":"name2","tags":"tags","configJson": {"key": "value"}}""")))
            @Valid @RequestBody ProjectUpdateDTO updateDto) throws UpdateFailedException {
        projectService.updateProject(id, updateDto);
    }

    @Operation(summary = "Query list of project information")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProjectListDTO.class))
                    ))
    )
    @GetMapping("/list")
    public List<ProjectListDTO> queryProject(
            @Parameter(
                    description = "Tenant condition",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam(value = "tenant") String tenant,
            @Parameter(
                    description = "Optional coeCode condition",
                    schema = @Schema(type = "string", maxLength = 128)
            )
            @RequestParam(value = "coeCode", required = false) String coeCode,
            @Parameter(
                    description = "Optional name wildcard condition",
                    schema = @Schema(type = "string", maxLength = 256)
            )
            @RequestParam(value = "nameLike", required = false) String nameLike,
            @Parameter(
                    description = "Optional tags condition",
                    schema = @Schema(type = "string", maxLength = 128)
            )
            @RequestParam(value = "tags", required = false) List<String> tags,
            @Parameter(
                    description = "Optional type condition",
                    schema = @Schema(type = "string", allowableValues = {"1", "2", "3", "4", "5"})
            )
            @RequestParam(value = "type") String type
    ) {
        return projectService.queryProject(tenant, nameLike, coeCode, type, tags);
    }

    @Operation(summary = "Get a project by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Data with given id not exist", content = @Content)
    })
    @GetMapping("/{id}")
    public ProjectDetailDTO getProjectById(
            @Parameter(
                    description = "project id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer id) throws DataNotExistException {
        return projectService.getProjectById(id);
    }
}
