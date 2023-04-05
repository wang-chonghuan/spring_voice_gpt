package com.waltwang.maivc.pojo;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageApi {
    private String role;
    private String content;
}
