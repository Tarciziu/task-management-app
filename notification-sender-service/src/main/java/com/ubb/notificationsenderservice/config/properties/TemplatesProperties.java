package com.ubb.notificationsenderservice.config.properties;

import com.ubb.notificationsenderservice.common.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.messaging.templates")
public class TemplatesProperties {
    private EmailTemplateByNotificationType email;

    @Setter
    @Getter
    public static class EmailTemplateByNotificationType extends HashMap<NotificationType, EmailTemplateDetails> {

    }

    @Setter
    @Getter
    public static class EmailTemplateDetails {
        private String subject;
        private String content;
    }
}
