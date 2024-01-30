package com.ubb.userservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.userservice.config.properties.JwtProperties;
import com.ubb.userservice.config.properties.MessagingProperties;
import com.ubb.userservice.config.properties.SecurityProperties;
import com.ubb.userservice.config.security.filters.JwtFilter;
import com.ubb.userservice.config.security.matcher.SkipPathRequestAntMatcher;
import com.ubb.userservice.mapper.UserMapper;
import com.ubb.userservice.mapper.UserSettingsMapper;
import com.ubb.userservice.repository.UserSettingsRepository;
import com.ubb.userservice.repository.UserRepository;
import com.ubb.userservice.service.auth.*;
import com.ubb.userservice.service.datetime.DateTimeService;
import com.ubb.userservice.service.datetime.DateTimeServiceImpl;
import com.ubb.userservice.service.notification.NotificationProcessorSenderService;
import com.ubb.userservice.service.notification.NotificationProcessorSenderServiceImpl;
import com.ubb.userservice.service.usersettings.UserSettingsService;
import com.ubb.userservice.service.usersettings.UserSettingsServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    // services
    @Bean
    public NotificationProcessorSenderService notificationProcessorSenderService(
            final RabbitTemplate rabbitTemplate,
            final ObjectMapper objectMapper
            ) {
        return new NotificationProcessorSenderServiceImpl(rabbitTemplate, objectMapper);
    }

    @Bean
    public AuthService authService(final AuthenticationManager authenticationManager,
                                   final JwtMakerService jwtMakerService,
                                   final UserRepository userRepository,
                                   final UserMapper userMapper,
                                   final PasswordEncoder encoder,
                                   final UserSettingsService userSettingsService,
                                   final MessagingProperties messagingProperties,
                                   final NotificationProcessorSenderService notificationProcessorSenderService) {
        return new AuthServiceImpl(authenticationManager, jwtMakerService, userRepository, userMapper,
                encoder, userSettingsService, notificationProcessorSenderService, messagingProperties);
    }

    @Bean
    public UserSettingsService userPreferencesService(final UserSettingsRepository userSettingsRepository,
                                                      final UserSettingsMapper userSettingsMapper) {
        return new UserSettingsServiceImpl(userSettingsRepository, userSettingsMapper);
    }

    @Bean
    public DateTimeService dateTimeService() {
        return new DateTimeServiceImpl();
    }

    @Bean
    public JWTVerifier jwtVerifier(final JwtProperties authProperties) {
        return JWT.require(Algorithm.HMAC512(authProperties.getSecret()))
                .build();
    }

    @Bean
    public JwtMakerService jwtMakerService(final JwtProperties authProperties,
                                           final DateTimeService dateTimeService,
                                           final JWTVerifier jwtVerifier) {
        return new JwtMakerServiceImpl(authProperties, dateTimeService, jwtVerifier);
    }

    @Bean
    public AppUserPrincipalService appUserPrincipalService() {
        return new AppUserPrincipalServiceImpl();
    }

    @Bean
    public UserDetailsService userDetailsService(final UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

    @Bean
    public JwtFilter jwtFilter(final JwtMakerService jwtMakerService,
                               final ObjectMapper objectMapper,
                               final UserDetailsService appUserDetailsService,
                               final SecurityProperties securityProperties) {
        final var skipPathRequestAntMatcher = new SkipPathRequestAntMatcher(
                securityProperties.getPublicEndpoints(), "/api/v1"
        );
        return new JwtFilter(jwtMakerService, skipPathRequestAntMatcher, objectMapper, appUserDetailsService);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
