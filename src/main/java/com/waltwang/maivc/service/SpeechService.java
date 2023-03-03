package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.pojo.ChatCompletionJson;
import com.waltwang.maivc.pojo.ChatRequestJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpeechService {
    public String parseReplyMessage(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatCompletionJson chatCompletionJson = objectMapper.readValue(responseBody, ChatCompletionJson.class);
        ChatCompletionJson.Choice.Message newMessage = chatCompletionJson.getChoices()[0].getMessage();
        log.info("received message content: {}", newMessage.getRole(), newMessage.getContent());
        return newMessage.getContent();
    }

    public void textToSpeech(String textMessage) {

    }
}
