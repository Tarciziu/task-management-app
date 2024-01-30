package com.ubb.taskmgmtservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {
    private MessagingQueues queues;

    @Setter
    @Getter
    public static class MessagingQueues {
        private String notifications;
    }
}
