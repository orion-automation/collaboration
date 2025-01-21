package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ConversationRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceBpmnNodeRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ConversationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.MessageDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    private final ResourceBpmnNodeRepository nodeRepository;

    public IdDTO<Long> saveConversation(String detailId, ConversationSaveDTO saveDTO) throws DataNotExistException {

        String activityId = saveDTO.getActivityId();
        String message = saveDTO.getMessage();

        //resourceDetailId 和 activityId 查询出对应的版本下的节点的唯一ID
        var node = nodeRepository.getOne(
                new LambdaQueryWrapper<ResourceBpmnNode>()
                        .eq(ResourceBpmnNode::getResourceDetailId, detailId)
                        .eq(ResourceBpmnNode::getActivityId, activityId)
        );
        if (Objects.nonNull(node)) {
            var nodeId = node.getId();
            Conversation conversation = new Conversation(nodeId, message);
            conversationRepository.save(conversation);
            return new IdDTO<>(conversation.getId());
        } else {
            throw new DataNotExistException("对应节点不存在！");
        }
    }

    public void updateConversationById(Long messageId, MessageDTO updateDTO) throws DataNotExistException {
        var dbConversation = conversationRepository.getById(messageId);
        if (Objects.nonNull(dbConversation)) {
            Conversation conversation = new Conversation();
            conversation.setId(messageId);
            conversation.setMessage(updateDTO.getMessage());
            conversationRepository.updateById(conversation);
        } else {
            throw new DataNotExistException("对应消息id不存在");
        }
    }

    public void deleteByConversationId(Long messageId) {
        conversationRepository.removeById(messageId);
    }

    public List<Conversation> getConversationList(String detailId, String activityId) {

        var bpmnNode = nodeRepository.getOne(
                new LambdaQueryWrapper<ResourceBpmnNode>()
                        .eq(ResourceBpmnNode::getActivityId, activityId)
                        .eq(ResourceBpmnNode::getResourceDetailId, detailId)
        );

        if (Objects.nonNull(bpmnNode)) {

            return conversationRepository.list(
                    new LambdaQueryWrapper<Conversation>()
                            .eq(Conversation::getNodeId, bpmnNode.getId())
            );
        }
        return new ArrayList<>();
    }
}
