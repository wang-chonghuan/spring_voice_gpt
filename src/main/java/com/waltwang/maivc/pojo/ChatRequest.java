package com.waltwang.maivc.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waltwang.maivc.domain.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// 这个ChatRequest要转换成api要求的格式，然后在http-post的地方被自动转成json
@Data
@AllArgsConstructor
public class ChatRequest {
    private String model = "gpt-3.5-turbo";
    private List<MessageApi> messages = new ArrayList<>();
    public ChatRequest(Conversation conversation) {
        // 从实体里取出聊天记录
        Map<String, Object> messages = conversation.getMessages();
        // 取出聊天记录的列表
        var messageList = (List<Map<String, Object>>) messages.get("messages");
        // 上面取出来的格式其实是MessageBody，为了传给gpt-api，要转成MessageApi的格式
        for(var messageJson : messageList) {
            //! 如果Message是@Value,则这里会报错
            MessageBody msgBody = new ObjectMapper().convertValue(messageJson, MessageBody.class);
            MessageApi msgApi = msgBody.extractMessageApi();
            this.messages.add(msgApi);
        }
    }

    public ChatRequest() {
        Arrays.asList(new MessageApi("user", "你好，你是谁？"));
    }
}

/*
{
  "model": "gpt-3.5-turbo",
  "messages": [{"role": "user", "content": "Hello!"}]
}
https://platform.openai.com/docs/api-reference/chat/create
 */