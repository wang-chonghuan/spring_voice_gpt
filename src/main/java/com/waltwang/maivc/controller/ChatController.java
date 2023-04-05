package com.waltwang.maivc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.waltwang.maivc.pojo.MessageBody;
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
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageBody messageBody) throws JsonProcessingException {
        MessageBody recvMsgBody = chatService.processUsermMessage(messageBody);
        return ResponseEntity.ok().body(recvMsgBody);
    }
    // http://localhost:8080/chat/healthcheck
    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body("ShellGPT service is running");
    }
}
