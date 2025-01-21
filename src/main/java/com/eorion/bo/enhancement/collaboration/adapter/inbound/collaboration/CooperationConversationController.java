package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ConversationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.MessageDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration")
@RequiredArgsConstructor
public class CooperationConversationController {

    private final ConversationService conversationService;

    /**
     * 对流程图中的某个节点创建消息
     */
    @PostMapping("/resource/detail/{detailId}/message")
    public IdDTO<Long> saveConversation(@PathVariable String detailId, @RequestBody ConversationSaveDTO conversation) throws DataNotExistException {
        return conversationService.saveConversation(detailId, conversation);
    }

    @PutMapping("/resource/detail/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConversationById(@PathVariable Long messageId,@RequestBody MessageDTO updateDTO) throws DataNotExistException {
        conversationService.updateConversationById(messageId, updateDTO);
    }

    @GetMapping("/resource/detail/{detailId}/message")
    public List<Conversation> getConversations(@PathVariable String detailId, @RequestParam("activityId") String activityId){
        return conversationService.getConversationList(detailId, activityId);
    }

    @DeleteMapping("/resource/detail/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByConversationId(@PathVariable Long messageId){
        conversationService.deleteByConversationId(messageId);
    }
}
