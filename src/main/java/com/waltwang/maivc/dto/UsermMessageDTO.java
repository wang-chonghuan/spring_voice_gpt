package com.waltwang.maivc.dto;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class UsermMessageDTO {
    long usermId;
    String message;
}
