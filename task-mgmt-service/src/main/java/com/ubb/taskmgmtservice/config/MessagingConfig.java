package com.ubb.taskmgmtservice.config;

import com.ubb.taskmgmtservice.config.properties.MessagingProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
    @Bean
    public Queue notificationsQueue(final MessagingProperties messagingProperties) {
        return new Queue(messagingProperties.getQueues().getNotifications(), true);
    }

    @Bean
    public TopicExchange notificationsTopicExchange(final MessagingProperties messagingProperties) {
        return new TopicExchange(String.format("%s-exchange", messagingProperties.getQueues().getNotifications()));
    }

    @Bean
    public Binding notificationsBinding(final Queue notificationsQueue, final TopicExchange notificationsTopicExchange) {
        return BindingBuilder.bind(notificationsQueue)
                .to(notificationsTopicExchange)
                .with("notifications.#");
    }
}
