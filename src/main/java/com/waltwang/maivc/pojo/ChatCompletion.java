package com.waltwang.maivc.pojo;

import lombok.Data;

@Data
public class ChatCompletion {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private Choice[] choices;

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }

    @Data
    public static class Choice {
        private Message message;
        private String finish_reason;
        private int index;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }
}

/*
{
  "id": "chatcmpl-6pmNL7mBdpJPemmSITPcQpGu5KRTg",
  "object": "chat.completion",
  "created": 1677798703,
  "model": "gpt-3.5-turbo-0301",
  "usage": {
    "prompt_tokens": 15,
    "completion_tokens": 26,
    "total_tokens": 41
  },
  "choices": [
    {
      "message": {
        "role": "assistant",
        "content": "\n\n我是AI助手，叫做OpenAI，可以为您提供帮助。"
      },
      "finish_reason": "stop",
      "index": 0
    }
  ]
}
*/
