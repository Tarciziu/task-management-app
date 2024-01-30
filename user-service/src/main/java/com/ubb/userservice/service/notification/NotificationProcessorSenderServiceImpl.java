package com.ubb.userservice.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.userservice.common.enums.ErrorCode;
import com.ubb.userservice.config.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@AllArgsConstructor
public class NotificationProcessorSenderServiceImpl implements NotificationProcessorSenderService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> void sendMessage(String queue, T message) {
        String dataAsString;
        try {
            dataAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Error while converting the message. {}", e.getMessage(), e);
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // start it on a different thread
        new Thread(() -> this.rabbitTemplate.convertSendAndReceive(
                queue,
                dataAsString
        )).start();
    }
}
