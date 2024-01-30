package com.ubb.notificationprocessorservice.common.dto;

import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;
import com.ubb.notificationprocessorservice.common.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@ToString
public class NotificationProcessorReceiverDto {
    private NotificationType notificationType;
    private Set<NotificationChannel> notificationChannels; // target notification channel
    private String userId;
    private List<NotificationDetailsItemDto> details;

    @Builder
    @Getter
    @ToString
    public static class NotificationDetailsItemDto {
        private String key;
        private String value;
    }
}
