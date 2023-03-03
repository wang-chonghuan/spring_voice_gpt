package com.waltwang.maivc.service;

import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    public Conversation getOrCreateConversationByUsermId(Long usermId) {
        var conv = conversationRepository.findFirstByUsermId(usermId);
        if(conv == null) {
            var newConv = new Conversation();
            newConv.setUsermId(usermId);
            Map<String, Object> messages = new HashMap<>();
            messages.put("messages", Collections.emptyList());
            newConv.setMessages(messages);
            return newConv;
        } else {
            return conv;
        }
    }
}
