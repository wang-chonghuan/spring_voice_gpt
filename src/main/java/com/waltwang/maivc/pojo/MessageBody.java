package com.waltwang.maivc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 客户端传过来的message结构体
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    private String username;
    private String role;
    private String content;
    private long datetime;
    private String sessionId;

    // 把MessageBody里的内容抽取出来一个MessageStore
    public MessageStore extractMessageStore(long usermId) {
        return new MessageStore(usermId, role, content, datetime);
    }
}
