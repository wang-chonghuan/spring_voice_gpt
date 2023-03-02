package com.waltwang.maivc.pojo;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
// @ToString(exclude = "age")
public class UsermMessageDTO {
    long usermId;
    String message;
}
