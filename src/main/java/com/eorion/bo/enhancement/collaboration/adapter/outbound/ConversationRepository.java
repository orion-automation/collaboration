package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.mapper.ConversationMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ConversationRepository extends ServiceImpl<ConversationMapper, Conversation> {
}
