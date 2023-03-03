package com.waltwang.maivc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestJson {
    private String model;
    private List<Message> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}

/*
{
  "model": "gpt-3.5-turbo",
  "messages": [{"role": "user", "content": "Hello!"}]
}
https://platform.openai.com/docs/api-reference/chat/create
 */