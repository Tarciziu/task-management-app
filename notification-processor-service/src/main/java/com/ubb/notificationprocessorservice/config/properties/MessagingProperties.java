package com.ubb.notificationprocessorservice.config.properties;

import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.messaging")
public class MessagingProperties {
    private MessagingQueues queues;
    private MessagingTopics topics;

    @Setter
    @Getter
    public static class MessagingQueues {
        private String notifications;
    }

    @Setter
    @Getter
    public static class MessagingTopics {
        private TopicNameByChannelType notifications;

        @Setter
        @Getter
        public static class TopicNameByChannelType extends HashMap<NotificationChannel, String> {}
    }
}
