package com.ubb.taskmgmtservice.common.dto;

import com.ubb.taskmgmtservice.common.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationProcessorSenderRequestDto {
    private NotificationType notificationType;
    private String userId;
    private List<NotificationDetailsItemDto> details;

    @Builder
    @Getter
    public static class NotificationDetailsItemDto {
        private String key;
        private String value;
    }
}
