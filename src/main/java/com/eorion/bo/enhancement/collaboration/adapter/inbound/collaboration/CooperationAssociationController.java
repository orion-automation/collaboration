package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.AssociationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOLong;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import com.eorion.bo.enhancement.collaboration.service.AssociationService;
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

/**
 * 资源关联
 */
@RestController
@RequestMapping("/enhancement/collaboration")
@Tag(name = "Collaboration Management - Association")
public class CooperationAssociationController {

    private final AssociationService associationService;

    public CooperationAssociationController(AssociationService associationService) {
        this.associationService = associationService;
    }

    @Operation(summary = "Create an association to a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTOLong.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Error when inserting",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping("/resource/{resourceId}/association")
    public IdDTO<Long> saveAssociation(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long resourceId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Association information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssociationSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {"name":"runtimeService","url":"https://www.google.com","type": "1"}""")))
            @RequestBody @Valid AssociationSaveDTO saveDTO) throws InsertFailedException {
        return associationService.saveAssociation(resourceId, saveDTO);
    }

    @Operation(summary = "Update an association of a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Association with given id not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error when updating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/resource/association/{associationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(
            @Parameter(
                    description = "association id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long associationId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Association information to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssociationSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {"name":"runtimeService","url":"https://www.google.com","type": "2"}""")))
            @Valid @RequestBody AssociationSaveDTO saveDTO) throws DataNotExistException, UpdateFailedException {
        associationService.updateAssociationById(associationId, saveDTO);
    }

    @Operation(summary = "Get all associations of a resource")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Association.class))
                    ))
    )
    @GetMapping("/resource/{resourceId}/association")
    public List<Association> getListForAssociation(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long resourceId) {
        return associationService.getListByResourceId(resourceId);
    }

    @Operation(summary = "Delete an association by id")
    @ApiResponses(
            @ApiResponse(responseCode = "204", description = "Success")
    )
    @DeleteMapping("/resource/association/{associationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(
            @Parameter(
                    description = "association id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long associationId) {
        associationService.removeAssociationById(associationId);
    }
}
