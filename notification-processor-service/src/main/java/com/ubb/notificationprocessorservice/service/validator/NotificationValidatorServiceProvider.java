package com.ubb.notificationprocessorservice.service.validator;

import com.ubb.notificationprocessorservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NotificationValidatorServiceProvider {
    private final List<NotificationValidatorService> notificationValidatorServices;

    public boolean isValid(final NotificationChannel notificationChannel,
                           final NotificationProcessorReceiverDto notificationProcessorReceiverDto) {
        return notificationValidatorServices
                .stream()
                .filter(value -> value.getNotificationChannel() == notificationChannel)
                .findFirst()
                .map(value -> value.isValid(notificationProcessorReceiverDto))
                .orElse(true);
    }
}
