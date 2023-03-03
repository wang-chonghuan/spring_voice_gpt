package com.waltwang.maivc.repository;

import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.domain.Userm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findFirstByUsermId(Long usermId);
}
