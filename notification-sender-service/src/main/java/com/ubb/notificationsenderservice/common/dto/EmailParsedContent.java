package com.ubb.notificationsenderservice.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmailParsedContent {
    private String to;
    private String subject;
    private String content;
}
