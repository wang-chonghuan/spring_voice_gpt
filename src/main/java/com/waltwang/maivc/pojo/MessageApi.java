package com.waltwang.maivc.pojo;

import lombok.*;

// 用来传给chatgptapi的消息结构体
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageApi {
    private String role;
    private String content;
}
