package com.ubb.notificationsenderservice.config;

import com.ubb.notificationsenderservice.config.properties.TemplatesProperties;
import com.ubb.notificationsenderservice.service.notification.email.EmailSenderService;
import com.ubb.notificationsenderservice.service.notification.email.EmailSenderServiceImpl;
import com.ubb.notificationsenderservice.service.notification.parser.EmailTemplateParser;
import com.ubb.notificationsenderservice.service.notification.parser.EmailTemplateParserImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AppConfig {
    @Bean
    public EmailSenderService emailSenderService(@Value("${spring.mail.username}") final String from, final JavaMailSender javaMailSender) {
        return new EmailSenderServiceImpl(javaMailSender, from);
    }

    @Bean
    public EmailTemplateParser emailTemplateParser(final TemplatesProperties templatesProperties) {
        return new EmailTemplateParserImpl(templatesProperties);
    }
}
