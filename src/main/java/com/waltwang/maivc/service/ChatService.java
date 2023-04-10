package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.domain.Conversation;
import com.waltwang.maivc.domain.Userm;
import com.waltwang.maivc.pojo.MessageApi;
import com.waltwang.maivc.pojo.MessageBody;
import com.waltwang.maivc.repository.ConversationRepository;
import com.waltwang.maivc.repository.UsermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private UsermRepository usermRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Value("${openai.gpt3_5url}")
    private String gpt3_5url;
    @Value("${openai.apikey_encrypted}")
    private String apikey_encrypted;
    private String apikey;
    @PostConstruct
    private void init() {
        apikey = new String(Base64.getDecoder().decode(apikey_encrypted), StandardCharsets.UTF_8);
    }

    /**
     * 1. 从DB读取聊天记录
     * 2. 把用户消息合并进聊天记录
     * 3. 把新聊天记录发给gpt
     * 4. 把gpt的回复合并进聊天记录,存入DB
     * 5. 把新回复发给用户
     */
    public MessageBody processUsermMessage(MessageBody msgBody) {
        // 1. 从DB读取聊天记录
        log.info("received msg: {}", msgBody.toString());
        Conversation conversation = findConversation(msgBody.getUsername(), msgBody.getSessionId());
        // 2. 把用户消息合并进聊天记录
        updateConversationByMessage(msgBody, conversation);
        // 3. 把新聊天记录发给gpt
        var responseBody = requestChatGPT(new ChatRequest(conversation)).getBody();
        log.info("responseBody: {}", responseBody);
        // 4. 把gpt的回复合并进聊天记录
        var responseMessage = saveConversation(responseBody, conversation, msgBody);
        // 5. 把新回复发给用户
        return responseMessage;
    }

    private ResponseEntity<String> requestChatGPT(ChatRequest chatRequest) {
        // 获取url和key
        String endpointUrl = gpt3_5url;
        // 设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apikey);
        // 设置HttpEntity
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);
        log.info("requestBody: {}", requestEntity.toString());
        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(endpointUrl, requestEntity, String.class);
    }

    private Conversation findConversation(String username, String sessionId) {
        Userm userm = usermRepository.findByUsername(username).get();
        return conversationRepository.getOrCreateConversation(userm.getId(), sessionId);
    }

    // 把当前收到的消息变成json,加入数据库里的聊天记录里，放到Conversation里，但是还没提交
    private void updateConversationByMessage(MessageBody msgBody, Conversation conversation) {
        // append json to json list
        var messages = conversation.getMessages();
        var messageListWithNew = new ArrayList<>((List<Map<String, Object>>) messages.get("messages"));
        var newMessage = msgBody.extractMessageStore(conversation.getUsermId());
        var newMessageJson = new ObjectMapper().convertValue(newMessage, Map.class);
        messageListWithNew.add(newMessageJson);
        messages.put("messages", messageListWithNew);
    }

    // 从gpt-api返回的结构里取出MessageApi
    private MessageApi findMessageFromApi(String responseBody) {
        // retrieve the message value from the JSON string responseBody
        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode messageJsonNode = rootNode.path("choices").get(0).path("message");
        MessageApi message = new ObjectMapper().convertValue(messageJsonNode, MessageApi.class);
        return message;
    }

    // 根据返回值生成recvMsgBody并且保存在数据库里，再返回出去
    private MessageBody saveConversation(String responseBody, Conversation conversation, MessageBody sendMsgBody) {
        var msgApi = findMessageFromApi(responseBody);
        MessageBody recvMsgBody = new MessageBody(
                sendMsgBody.getUsername(),
                msgApi.getRole(),
                msgApi.getContent(),
                System.currentTimeMillis(),
                sendMsgBody.getSessionId()
        );
        updateConversationByMessage(recvMsgBody, conversation);
        conversationRepository.save(conversation);
        return recvMsgBody;
    }
}