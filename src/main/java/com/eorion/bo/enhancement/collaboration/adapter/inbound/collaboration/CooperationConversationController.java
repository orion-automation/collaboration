package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ConversationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.MessageDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.openapi.IdDTOLong;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.service.ConversationService;
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
@RequestMapping("/enhancement/collaboration")
@Tag(name = "Collaboration Management - Message")
public class CooperationConversationController {

    private final ConversationService conversationService;

    public CooperationConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * 对流程图中的某个节点创建消息
     */
    @Operation(summary = "Create a message to a resource detail")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IdDTOLong.class))),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Resource detail with given id and/or node with given activity id not exist", content = @Content),
    })
    @PostMapping("/resource/detail/{detailId}/message")
    public IdDTO<Long> saveConversation(
            @Parameter(
                    description = "resource detail id",
                    example = "1", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String detailId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Message to create", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ConversationSaveDTO.class),
                            examples = @ExampleObject(value = """
                                    {"activityId":"Activity_ID","message":"Message"}""")))
            @Valid @RequestBody ConversationSaveDTO conversation) throws DataNotExistException {
        return conversationService.saveConversation(detailId, conversation);
    }

    @Operation(summary = "Update a message by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request body validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Resource detail with given id and/or node with given activity id not exist", content = @Content),
    })
    @PutMapping("/resource/detail/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConversationById(
            @Parameter(
                    description = "message id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long messageId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Message to update", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageDTO.class),
                            examples = @ExampleObject(value = """
                                    {"message":"Another message"}""")))
            @Valid @RequestBody MessageDTO updateDTO) throws DataNotExistException {
        conversationService.updateConversationById(messageId, updateDTO);
    }

    @Operation(summary = "Get all messages of a resource detail")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class))
                    ))
    )
    @GetMapping("/resource/detail/{detailId}/message")
    public List<Conversation> getConversations(
            @Parameter(
                    description = "resource detail id",
                    example = "1", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String detailId,
            @Parameter(
                    description = "activity id",
                    example = "Activity_ID", required = true,
                    schema = @Schema(type = "string", maxLength = 64)
            )
            @RequestParam("activityId") String activityId) {
        return conversationService.getConversationList(detailId, activityId);
    }

    @Operation(summary = "Delete a message by id")
    @ApiResponses(
            @ApiResponse(responseCode = "204", description = "Success")
    )
    @DeleteMapping("/resource/detail/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByConversationId(
            @Parameter(
                    description = "message id",
                    example = "1", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long messageId) {
        conversationService.deleteByConversationId(messageId);
    }
}
