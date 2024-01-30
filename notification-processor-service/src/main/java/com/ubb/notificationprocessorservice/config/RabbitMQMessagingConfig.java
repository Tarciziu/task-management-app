package com.ubb.notificationprocessorservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.notificationprocessorservice.config.properties.MessagingProperties;
import com.ubb.notificationprocessorservice.repository.UserSettingsRepository;
import com.ubb.notificationprocessorservice.service.receiver.NotificationReceiver;
import com.ubb.notificationprocessorservice.service.sender.NotificationSenderService;
import com.ubb.notificationprocessorservice.service.validator.NotificationValidatorServiceProvider;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQMessagingConfig {
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

    @Bean
    public SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
                                                    final MessageListenerAdapter listenerAdapter,
                                                    final MessagingProperties messagingProperties) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(messagingProperties.getQueues().getNotifications());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public NotificationReceiver notificationReceiver(final ObjectMapper objectMapper,
                                                     final UserSettingsRepository userSettingsRepository,
                                                     final NotificationValidatorServiceProvider notificationValidatorServiceProvider,
                                                     final NotificationSenderService notificationSenderService,
                                                     final MessagingProperties messagingProperties
    ) {
        return new NotificationReceiver(objectMapper, userSettingsRepository, notificationValidatorServiceProvider,
                notificationSenderService, messagingProperties);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(NotificationReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
