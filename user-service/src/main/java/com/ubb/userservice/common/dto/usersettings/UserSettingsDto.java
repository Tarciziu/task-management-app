package com.ubb.userservice.common.dto.usersettings;

import com.ubb.userservice.common.enums.NotificationChannel;
import com.ubb.userservice.common.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class UserSettingsDto {
    private Map<NotificationType, List<NotificationTypeStatus>> notifications;

    @Setter
    @Getter
    public static class NotificationTypeStatus {
        private NotificationChannel channel;
        private boolean enabled;
    }
}
