package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.pojo.ChatCompletion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpeechService {
    public String parseReplyMessage(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatCompletion chatCompletion = objectMapper.readValue(responseBody, ChatCompletion.class);
        String content = chatCompletion.getChoices()[0].getMessage().getContent();
        log.info("received message content: {}", content);
        return content;
    }

    public void textToSpeech(String textMessage) {

    }
}
