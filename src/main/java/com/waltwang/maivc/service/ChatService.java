package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.domain.Userm;
import com.waltwang.maivc.pojo.ChatRequest;
import com.waltwang.maivc.pojo.Message;
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

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private UsermRepository usermRepository;
    @Autowired
    private ConversationRepository conversationRepository;

    /**
     * 1. 从DB读取聊天记录
     * 2. 把用户消息合并进聊天记录
     * 3. 把新聊天记录发给gpt
     * 4. 把gpt的回复合并进聊天记录,存入DB
     * 5. 把新回复发给用户
     * @param usermMessageDTO
     */
    public Message processUsermMessage(UsermMessageDTO usermMessageDTO) {
        // 1. 从DB读取聊天记录
        log.info("received msg: {}", usermMessageDTO.toString());
        Conversation conversation = findConversation(usermMessageDTO.getUsermId());
        // 2. 把用户消息合并进聊天记录
        updateConversationByMessage(usermMessageDTO.getMessage(), conversation);
        // 3. 把新聊天记录发给gpt
        var responseBody = requestChatGPT(new ChatRequest(conversation)).getBody();
        log.info("responseBody: {}", responseBody);
        // 4. 把gpt的回复合并进聊天记录
        var responseMessage = saveConversation(responseBody, conversation);
        // 5. 把新回复发给用户
        return responseMessage;
    }

    private ResponseEntity<String> requestChatGPT(ChatRequest chatRequest) {
        // 获取url和key
        String endpointUrl = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        // 设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        // 设置HttpEntity
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);
        log.info("requestBody: {}", requestEntity.toString());
        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpointUrl, requestEntity, String.class);
        return responseEntity;
    }

    private Conversation findConversation(long usermId) {
        Userm userm = usermRepository.findById(usermId).get();
        Conversation conversation = conversationRepository
                .getOrCreateConversationByUsermId(userm.getId());
        return conversation;
    }

    private void updateConversationByMessage(Message message, Conversation conversation) {
        // append json to json list
        Map<String, Object> messages = conversation.getMessages();
        List<Map<String, Object>> newMessageList = new ArrayList<>(
                (List<Map<String, Object>>) messages.get("messages"));
        newMessageList.add(new ObjectMapper().convertValue(message, Map.class));
        messages.put("messages", newMessageList);
    }

    private Message findMessage(String responseBody) {
        // retrieve the message value from the JSON string responseBody
        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode messageJsonNode = rootNode.path("choices").get(0).path("message");
        Message message = new ObjectMapper().convertValue(messageJsonNode, Message.class);
        return message;
    }

    private Message saveConversation(String responseBody, Conversation conversation) {
        var message = findMessage(responseBody);
        updateConversationByMessage(message, conversation);
        conversationRepository.save(conversation);
        return message;
    }
}