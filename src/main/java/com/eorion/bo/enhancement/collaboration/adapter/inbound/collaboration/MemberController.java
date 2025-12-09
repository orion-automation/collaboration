package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberDeleteDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.MemberStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOInteger;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.MemberDetailDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.service.MemberService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enhancement/collaboration/member")
@Tag(name = "Collaboration Management - Member")
public class MemberController {
    private final MemberService memberService;
    private final MemberStructureMapper mapper;

    public MemberController(MemberService memberService, MemberStructureMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @Operation(summary = "Create member information")
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
    public IdDTO<Integer> saveMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Member information to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {"projectId": 1,"userId":"demo","role":"1"}""")))
            @Valid @RequestBody MemberSaveDTO dto) throws InsertFailedException {
        return memberService.saveOrUpdate(mapper.saveDtoToEntity(dto));
    }

    @Operation(summary = "Delete a member with given project id or/and user id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body format validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "project id or/and user id", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberDeleteDTO.class),
                            examples = @ExampleObject(value = """
                                    {"projectId": 1,"userId":"demo"}""")))
            @Valid @RequestBody MemberDeleteDTO deleteDto) {
        memberService.deleteMember(deleteDto);
    }

    @Operation(summary = "Query list of members with given project id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = MemberDetailDTO.class))
                    )),
            @ApiResponse(responseCode = "404", description = "No members exist with given project id", content = @Content),
    })
    @GetMapping("/list/{projectId}")
    public List<MemberDetailDTO> queryMemberList(
            @Parameter(
                    description = "project id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int32", minimum = "1")
            )
            @PathVariable Integer projectId) throws DataNotExistException {
        return memberService.queryMemberList(projectId).stream().map(mapper::toMemberDetailDTO).collect(Collectors.toList());
    }
}
