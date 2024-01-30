package com.ubb.notificationprocessorservice.service.validator;

import com.ubb.notificationprocessorservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;

public interface NotificationValidatorService {
    boolean isValid(final NotificationProcessorReceiverDto notificationProcessorReceiverDto);
    NotificationChannel getNotificationChannel();
}
