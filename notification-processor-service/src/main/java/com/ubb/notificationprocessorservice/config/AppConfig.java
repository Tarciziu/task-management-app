package com.ubb.notificationprocessorservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.notificationprocessorservice.service.sender.KafkaNotificationSenderService;
import com.ubb.notificationprocessorservice.service.sender.NotificationSenderService;
import com.ubb.notificationprocessorservice.service.validator.EmailNotificationValidatorService;
import com.ubb.notificationprocessorservice.service.validator.NotificationValidatorService;
import com.ubb.notificationprocessorservice.service.validator.NotificationValidatorServiceProvider;
import com.ubb.notificationprocessorservice.service.validator.RealtimeNotificationValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public NotificationValidatorService emailNotificationValidatorService() {
        return new EmailNotificationValidatorService();
    }

    @Bean
    public NotificationValidatorService realtimeNotificationValidatorService() {
        return new RealtimeNotificationValidatorService();
    }

    @Bean
    public NotificationValidatorServiceProvider notificationValidatorServiceProvider(final List<NotificationValidatorService> notificationValidatorServices) {
        return new NotificationValidatorServiceProvider(notificationValidatorServices);
    }
}
