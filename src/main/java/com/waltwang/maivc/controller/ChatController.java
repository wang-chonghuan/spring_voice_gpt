package com.waltwang.maivc.controller;

import com.waltwang.maivc.dto.UsermMessageDTO;
import com.waltwang.maivc.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @RequestMapping(value="/message", method= RequestMethod.POST)
    public ResponseEntity<?> usermMessage(@RequestBody @Valid UsermMessageDTO usermMessageDTO) {
        chatService.processUsermMessage(usermMessageDTO);
        return ResponseEntity.ok().body("");
    }
}