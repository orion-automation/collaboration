package com.eorion.bo.enhancement.collaboration.adapter.inbound.pb;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CoopPublicResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceBriefDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.service.ResourceDetailService;
import com.eorion.bo.enhancement.collaboration.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/pb/enhancement/collaboration/resource")
@Validated
@Tag(name = "Collaboration Management - Resource (public access)")
public class PublicResourceController {
    private final ResourceService resourceService;
    private final ResourceDetailService detailService;

    public PublicResourceController(ResourceService resourceService, ResourceDetailService detailService) {
        this.resourceService = resourceService;
        this.detailService = detailService;
    }

    @Operation(summary = "Get a resource brief by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceBriefDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
    })
    @GetMapping("/{resourceId}")
    public ResourceBriefDTO getResourceById(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer resourceId) throws DataNotExistException {
        return resourceService.getResourceBrieById(resourceId);
    }

    @Operation(summary = "Get a resource detail by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CoopPublicResourceDetailDTO.class))),
            @ApiResponse(responseCode = "403", description = "Password wrong", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource with given id not exist", content = @Content),
    })
    @PostMapping("/detail/{detailId}")
    public ResponseEntity<?> getResourceDetailById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String detailId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceDetailPasswordDTO.class),
                            examples = @ExampleObject(value = """
                                    {"password": "password"}""")))
            @Validated @RequestBody(required = false) ResourceDetailPasswordDTO passwordDTO) throws DataNotExistException, NoSuchAlgorithmException {
        return detailService.getPublicResourceDetailById(detailId, passwordDTO);
    }
}
