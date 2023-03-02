package com.waltwang.maivc.service;

import com.waltwang.maivc.dto.UsermMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    public void processUsermMessage(UsermMessageDTO usermMessageDTO) {
        log.info("received msg: {}", usermMessageDTO.toString());
    }
}
