package com.ubb.taskmgmtservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.taskmgmtservice.config.properties.MessagingProperties;
import com.ubb.taskmgmtservice.config.properties.SecurityProperties;
import com.ubb.taskmgmtservice.config.security.filters.XUserIdFilter;
import com.ubb.taskmgmtservice.config.security.matcher.SkipPathRequestAntMatcher;
import com.ubb.taskmgmtservice.mapper.TaskMapper;
import com.ubb.taskmgmtservice.repository.TaskRepository;
import com.ubb.taskmgmtservice.repository.UserRepository;
import com.ubb.taskmgmtservice.service.auth.AppUserDetailsService;
import com.ubb.taskmgmtservice.service.auth.AppUserPrincipalService;
import com.ubb.taskmgmtservice.service.auth.AppUserPrincipalServiceImpl;
import com.ubb.taskmgmtservice.service.notification.NotificationProcessorSenderService;
import com.ubb.taskmgmtservice.service.notification.NotificationProcessorSenderServiceImpl;
import com.ubb.taskmgmtservice.service.task.TaskService;
import com.ubb.taskmgmtservice.service.task.TaskServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class AppConfig {
    @Bean
    public XUserIdFilter jwtFilter(final ObjectMapper objectMapper,
                                   final UserDetailsService appUserDetailsService,
                                   final SecurityProperties securityProperties) {
        final var skipPathRequestAntMatcher = new SkipPathRequestAntMatcher(
                securityProperties.getPublicEndpoints(), "/api/v1"
        );
        return new XUserIdFilter(skipPathRequestAntMatcher, objectMapper, appUserDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(final UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

    @Bean
    public AppUserPrincipalService appUserPrincipalService() {
        return new AppUserPrincipalServiceImpl();
    }

    @Bean
    public TaskService taskService(final TaskRepository taskRepository,
                                   final TaskMapper taskMapper,
                                   final MessagingProperties messagingProperties,
                                   final NotificationProcessorSenderService notificationProcessorSenderService) {
        return new TaskServiceImpl(taskRepository, taskMapper, messagingProperties, notificationProcessorSenderService);
    }

    @Bean
    public NotificationProcessorSenderService notificationProcessorSenderService(
            final RabbitTemplate rabbitTemplate,
            final ObjectMapper objectMapper
            ) {
        return new NotificationProcessorSenderServiceImpl(rabbitTemplate, objectMapper);
    }
}
