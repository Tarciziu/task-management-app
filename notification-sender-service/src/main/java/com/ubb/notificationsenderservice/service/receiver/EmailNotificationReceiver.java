package com.ubb.notificationsenderservice.service.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.notificationsenderservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationsenderservice.service.notification.email.EmailSenderService;
import com.ubb.notificationsenderservice.service.notification.parser.EmailTemplateParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@AllArgsConstructor
public class EmailNotificationReceiver {
    private final ObjectMapper objectMapper;
    private final EmailTemplateParser emailTemplateParser;
    private final EmailSenderService emailSenderService;

    @KafkaListener(topics = "${app.messaging.topics.notifications.EMAIL}")
    public void receiveMessage(String message) {
        NotificationProcessorReceiverDto input;
        try {
            input = objectMapper.readValue(message, NotificationProcessorReceiverDto.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return;
        }

        final var parsedContent = emailTemplateParser.parseTemplate(input.getNotificationType(), input);
        if(parsedContent.isEmpty()) {
            log.error("Content count not be parsed. No email will be sent.");
            return;
        }

        emailSenderService.sendEmail(
                parsedContent.get().getTo(),
                parsedContent.get().getSubject(),
                parsedContent.get().getContent()
        );
    }
}
