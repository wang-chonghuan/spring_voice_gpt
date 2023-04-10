package com.waltwang.maivc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 用户存储到db里的消息结构
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageStore {
    private long usermId;
    private String role;
    private String content;
    private long datetime;

    // 把MessageBody里的内容抽取出来一个MessageApi
    public MessageApi extractMessageApi() {
        return new MessageApi(this.getRole(), this.getContent());
    }
}
