package com.ubb.userservice.common.dto;

import com.ubb.userservice.common.enums.NotificationChannel;
import com.ubb.userservice.common.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@Builder
public class NotificationProcessorSenderRequestDto {
    private NotificationType notificationType;
    private Set<NotificationChannel> notificationChannels; // force particular channels
    private String userId;
    private List<NotificationDetailsItemDto> details;

    @Builder
    @Getter
    public static class NotificationDetailsItemDto {
        private String key;
        private String value;
    }
}
