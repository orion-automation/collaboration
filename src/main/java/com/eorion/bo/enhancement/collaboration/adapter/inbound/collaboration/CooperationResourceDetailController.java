package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOString;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CheckPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CoopResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.ResourceConflictException;
import com.eorion.bo.enhancement.collaboration.service.ResourceDetailService;
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
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration")
@Validated
@Tag(name = "Collaboration Management - Resource Detail")
public class CooperationResourceDetailController {

    private final ResourceDetailService detailService;

    public CooperationResourceDetailController(ResourceDetailService detailService) {
        this.detailService = detailService;
    }

    /**
     * 针对某个协同资源下的不同版本xml存储
     *
     * @param resourceId 对应协同资源id
     * @param saveDTO
     * @return
     */
    @Operation(summary = "Create detail information to a resource")
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
    @PostMapping("/resource/{resourceId}/detail")
    public IdDTO<String> create(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32")
            )
            @PathVariable Integer resourceId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Resource detail to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceDetailSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "name":"name",
                                        "xml":"xml",
                                        "configJson":{"key2":"value2","key1":"value1"},
                                        "nodes":["Flow_1","Flow_0","Activity_1"]
                                    }""")))
            @Valid @RequestBody ResourceDetailSaveDTO saveDTO) throws InsertFailedException {
        return detailService.saveResourceDetail(resourceId, saveDTO);
    }

    /**
     * 通过 resourceDetailId 更新
     */
    @Operation(summary = "Update a resource detail by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Resource detail with given id has already been updated (optimistic lock occurred)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PutMapping("/resource/detail/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateResourceDetailById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Resource detail to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceDetailUpdateDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "name":"name",
                                        "xml":"xml",
                                        "configJson":{"key2":"value2","key1":"value1"},
                                        "nodes":["Flow_1","Flow_0","Activity_1"],
                                        "currentVersionTimestamp": 1683881561254
                                    }""")))
            @Validated @RequestBody ResourceDetailUpdateDTO saveDTO) throws ResourceConflictException {
        detailService.updateResourceDetailById(id, saveDTO);
    }

    /**
     * 查询通过某个协同资源的resourceId，查询所有的版本的resourceDetail
     *
     * @return
     */
    @Operation(summary = "Get all detail information of a resource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CoopResourceDetailDTO.class))
                    )),
    })
    @GetMapping("/resource/{resourceId}/detail")
    public List<CoopResourceDetailDTO> getAllDetailByResourceId(
            @Parameter(
                    description = "resource id",
                    example = "1", required = true,
                    schema = @Schema(type = "string", format = "int32")
            )
            @PathVariable int resourceId) {
        return detailService.getAllDetailByResourceId(resourceId);
    }


    /**
     * 通过DetailId获取资源的Detail
     *
     * @return
     */
    @Operation(summary = "Get a resource detail information by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CoopResourceDetailDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Resource detail with given id not exist", content = @Content),
    })
    @GetMapping("/resource/detail/{resourceDetailId}")
    public CoopResourceDetailDTO getResourceDetailById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String resourceDetailId) throws DataNotExistException {
        return detailService.getResourceDetailById(resourceDetailId);
    }

    /**
     * 通过资源的Detail的id 创建公开访问的密码
     * POST /collaboration/resource/detail/{id}
     */
    @Operation(summary = "Create password to a resource detail for public access")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Resource detail with given id not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error when creating",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping("/resource/detail/{resourceDetailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPasswordById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String resourceDetailId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceDetailPasswordDTO.class),
                            examples = @ExampleObject(value = """
                                    {"password":"MyPassword"}""")))
            @Valid @RequestBody ResourceDetailPasswordDTO passwordDTO) throws DataNotExistException, NoSuchAlgorithmException {
        detailService.createPasswordById(resourceDetailId, passwordDTO);
    }

    /**
     * 通过资源的Detail的id 移除公开访问的密码
     */
    @Operation(summary = "Remove password of a resource detail")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping("/resource/detail/{resourceDetailId}/password/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPasswordById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String resourceDetailId) {
        detailService.removePasswordById(resourceDetailId);
    }

    /**
     * 通过资源的Detail 的id删除资源，将对应的所有资源删除
     */
    @Operation(summary = "Delete a resource detail together with all related information")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success")
    })
    @DeleteMapping("/resource/detail/{resourceDetailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeResourceDetailById(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @PathVariable String resourceDetailId) {
        detailService.removeResourceDetailById(resourceDetailId);
    }

    /**
     * 查询当前记录是否设置密码
     */
    @Operation(summary = "Check whether or not password has been set to a resource detail")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CheckPasswordDTO.class)))
    })
    @GetMapping("/resource/detail/checkPassword")
    public CheckPasswordDTO checkPasswordEmpty(
            @Parameter(
                    description = "resource detail id",
                    required = true,
                    example = "35f6289379cb71bf0cb356f306dced02",
                    schema = @Schema(pattern = "^[0-9a-fA-F]{32}$")
            )
            @Pattern(regexp = "^[0-9a-fA-F]{32}$", message = "must be UUID without hyphens")
            @RequestParam("resourceDetailId") String resourceDetailId) {
        return detailService.checkPasswordEmpty(resourceDetailId);
    }

}
