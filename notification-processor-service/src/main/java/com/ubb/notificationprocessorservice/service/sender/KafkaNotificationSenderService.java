package com.ubb.notificationprocessorservice.service.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@AllArgsConstructor
public class KafkaNotificationSenderService implements NotificationSenderService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public <T> void sendNotification(String topic, T message) {
        var messageAsString = "";
        try {
            messageAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return;
        }

        kafkaTemplate.send(topic, messageAsString);
    }
}
