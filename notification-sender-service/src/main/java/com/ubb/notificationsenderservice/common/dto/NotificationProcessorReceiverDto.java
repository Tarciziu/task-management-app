package com.ubb.notificationsenderservice.common.dto;

import com.ubb.notificationsenderservice.common.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class NotificationProcessorReceiverDto {
    private NotificationType notificationType;
    private List<NotificationDetailsItemDto> details;

    @Builder
    @Getter
    @ToString
    public static class NotificationDetailsItemDto {
        private String key;
        private String value;
    }
}
