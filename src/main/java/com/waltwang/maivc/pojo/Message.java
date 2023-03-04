package com.waltwang.maivc.pojo;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;
}
