package com.waltwang.maivc.repository;

import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.domain.Userm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findFirstByUsermIdAndSessionId(Long usermId, String sessionId);

    default public Conversation getOrCreateConversation(Long usermId, String sessionId) {
        var conv = this.findFirstByUsermIdAndSessionId(usermId, sessionId);
        if(conv == null) {
            var newConv = new Conversation();
            newConv.setUsermId(usermId);
            newConv.setSessionId(sessionId);
            Map<String, Object> messages = new HashMap<>();
            messages.put("messages", Collections.emptyList());
            newConv.setMessages(messages);
            return newConv;
        } else {
            return conv;
        }
    }
}
