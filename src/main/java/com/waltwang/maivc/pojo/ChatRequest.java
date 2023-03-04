package com.waltwang.maivc.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.domain.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ChatRequest {
    private String model = "gpt-3.5-turbo";
    private List<Message> messages = new ArrayList<>();
    public ChatRequest(Conversation conversation) {
        Map<String, Object> messages = conversation.getMessages();
        var messageList = (List<Map<String, Object>>) messages.get("messages");
        for(var messageJson : messageList) {
            //! 如果Message是@Value,则这里会报错
            Message message = new ObjectMapper().convertValue(messageJson, Message.class);
            this.messages.add(message);
        }
    }

    public ChatRequest() {
        Arrays.asList(new Message("user", "你好，你是谁？"));
    }
}

/*
{
  "model": "gpt-3.5-turbo",
  "messages": [{"role": "user", "content": "Hello!"}]
}
https://platform.openai.com/docs/api-reference/chat/create
 */