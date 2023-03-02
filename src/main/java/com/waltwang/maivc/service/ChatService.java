package com.waltwang.maivc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.pojo.ChatRequest;
import com.waltwang.maivc.pojo.UsermMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    public void processUsermMessage(UsermMessageDTO usermMessageDTO) throws JsonProcessingException {
        log.info("received msg: {}", usermMessageDTO.toString());
        // 获取url和key
        String endpointUrl = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        // 设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); //headers.set("Authorization", "Bearer " + apiKey);
        // 设置消息内容
        ChatRequest chatRequest = new ChatRequest(
                "gpt-3.5-turbo",
                Arrays.asList(new ChatRequest.Message("user", "Hello!"))
        );
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);
        log.info("requestBody: {}", requestEntity.toString());
        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpointUrl, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        log.info("responseBody: {}", responseBody);
    }
}
