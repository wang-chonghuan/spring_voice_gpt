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
    Conversation findFirstByUsermId(Long usermId);

    default public Conversation getOrCreateConversationByUsermId(Long usermId) {
        var conv = this.findFirstByUsermId(usermId);
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
