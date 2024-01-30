package com.ubb.notificationprocessorservice.service.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.notificationprocessorservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;
import com.ubb.notificationprocessorservice.config.properties.MessagingProperties;
import com.ubb.notificationprocessorservice.model.UserSettingsDocument;
import com.ubb.notificationprocessorservice.repository.UserSettingsRepository;
import com.ubb.notificationprocessorservice.service.sender.NotificationSenderService;
import com.ubb.notificationprocessorservice.service.validator.NotificationValidatorServiceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class NotificationReceiver {
    private final ObjectMapper objectMapper;
    private final UserSettingsRepository userSettingsRepository;
    private final NotificationValidatorServiceProvider notificationValidatorServiceProvider;
    private final NotificationSenderService notificationSenderService;
    private final MessagingProperties messagingProperties;

    public void receiveMessage(String message) {
        final NotificationProcessorReceiverDto convertedMessage;

        try {
            convertedMessage = objectMapper.readValue(
                    message,
                    NotificationProcessorReceiverDto.class
            );
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return;
        }

        if (Objects.isNull(convertedMessage.getNotificationType())) {
            log.error("Notification does not contain notificationType. Aborting...");
            return;
        }

        final var activeChannels = new ArrayList<NotificationChannel>();
        if (ObjectUtils.isEmpty(convertedMessage.getUserId())) {
            log.info("Notification does not contain userId.");
            if (!ObjectUtils.isEmpty(convertedMessage.getNotificationChannels())) {
                activeChannels.addAll(convertedMessage.getNotificationChannels());
            }
        } else {
            log.info("Notification contains userId.");
            final var userSettingsDocumentOptional = userSettingsRepository.findByUserId(convertedMessage.getUserId());
            if (userSettingsDocumentOptional.isEmpty()) {
                log.error("No user settings found for userId: {}. Aborting...", convertedMessage.getUserId());
                return;
            }

            final var notificationTypeStatusSettings = userSettingsDocumentOptional
                    .map(UserSettingsDocument::getNotifications)
                    .map(value -> value.get(convertedMessage.getNotificationType()))
                    .orElse(Collections.emptyList());

            final var activeChannelsFromSettings = notificationTypeStatusSettings
                    .stream()
                    .filter(UserSettingsDocument.NotificationTypeStatus::isEnabled)
                    .map(UserSettingsDocument.NotificationTypeStatus::getChannel)
                    .toList();
            activeChannels.addAll(activeChannelsFromSettings);
        }

        log.info("Active channels: {}", activeChannels);
        final var validChannels = activeChannels
                .stream()
                .filter(activeChannel -> {
                    final var isValid = notificationValidatorServiceProvider.isValid(activeChannel, convertedMessage);

                    if (!isValid) {
                        log.error("Input not valid for channel: {} and notificationType: {}", activeChannel, convertedMessage.getNotificationType());
                    }

                    return isValid;
                })
                .toList();

        log.info("Valid channels: {}", validChannels);
        for (var channel : validChannels) {
            final var notificationsTopic = messagingProperties.getTopics().getNotifications();
            final var topicNameByChannel = notificationsTopic.get(channel);
            final var newMessage = NotificationProcessorReceiverDto.builder()
                    .notificationType(convertedMessage.getNotificationType())
                    .details(convertedMessage.getDetails())
                    .build();
            notificationSenderService.sendNotification(topicNameByChannel, newMessage);
        }
    }
}
