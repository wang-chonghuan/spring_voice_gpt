package com.waltwang.maivc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    private long usermId;
    private String role;
    private String content;
    private long datetime;
    private String sessionId;

    // 把MessageBody里的内容抽取出来一个MessageApi
    public MessageApi extractMessageApi() {
        return new MessageApi(this.getRole(), this.getContent());
    }
}
