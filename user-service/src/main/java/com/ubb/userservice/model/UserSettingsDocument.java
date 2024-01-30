package com.ubb.userservice.model;

import com.ubb.userservice.common.dto.usersettings.UserSettingsDto;
import com.ubb.userservice.common.enums.NotificationChannel;
import com.ubb.userservice.common.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document("user-settings")
public class UserSettingsDocument {
    @Id
    private String id;

    private Map<NotificationType, List<NotificationTypeStatus>> notifications;

    private String userId;

    @Setter
    @Getter
    public static class NotificationTypeStatus {
        private NotificationChannel channel;
        private boolean enabled;
    }
}
