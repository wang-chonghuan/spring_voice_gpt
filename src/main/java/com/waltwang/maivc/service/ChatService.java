package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.domain.Userm;
import com.waltwang.maivc.pojo.ChatCompletionJson;
import com.waltwang.maivc.pojo.ChatRequestJson;
import com.waltwang.maivc.pojo.UsermMessageDTO;
import com.waltwang.maivc.repository.ConversationRepository;
import com.waltwang.maivc.repository.UsermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private UsermRepository usermRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    public void processUsermMessage(UsermMessageDTO usermMessageDTO) {
        log.info("received msg: {}", usermMessageDTO.toString());
        var responseEntity = requestChatGPT(usermMessageDTO);
        var responseBody = responseEntity.getBody();
        log.info("responseBody: {}", responseBody);
        updateConversationWithMessage(usermMessageDTO.getUsermId(), responseBody);
    }

    private ResponseEntity<String> requestChatGPT(UsermMessageDTO usermMessageDTO) {
        // 获取url和key
        String endpointUrl = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        // 设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); //headers.set("Authorization", "Bearer " + apiKey);
        // 设置消息内容
        ChatRequestJson chatRequestJson = new ChatRequestJson(
                "gpt-3.5-turbo",
                Arrays.asList(new ChatRequestJson.Message("user", "你好，你是谁？"))
        );
        HttpEntity<ChatRequestJson> requestEntity = new HttpEntity<>(chatRequestJson, headers);
        log.info("requestBody: {}", requestEntity.toString());
        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpointUrl, requestEntity, String.class);
        return responseEntity;
    }

    private void updateConversationWithMessage(Long usermId, String responseBody) {
        // retrieve the userm entity with the given usermId
        Userm userm = usermRepository.findById(usermId).get();

        // retrieve the conversation entity that the user belongs to
        var conversation = conversationRepository.getOrCreateConversationByUsermId(usermId);

        // retrieve the message value from the JSON string responseBody
        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        var message = rootNode.path("choices").get(0).path("message");

        // append the message to the conversation entity's messages array, must create a new modifiable list object
        Map<String, Object> messages = conversation.getMessages();
        List<Map<String, Object>> newMessageList = new ArrayList<>(
                (List<Map<String, Object>>) messages.get("messages"));
        Map<String, Object> newMessage = new ObjectMapper().convertValue(message, Map.class);
        newMessageList.add(newMessage);
        messages.put("messages", newMessageList);
        // save the updated conversation entity
        conversationRepository.save(conversation);
    }
}