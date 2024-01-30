package com.ubb.notificationprocessorservice.service.validator;

import com.ubb.notificationprocessorservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationprocessorservice.common.enums.NotificationChannel;

// TODO: implement logic here
public class EmailNotificationValidatorService implements NotificationValidatorService {
    @Override
    public boolean isValid(NotificationProcessorReceiverDto notificationProcessorReceiverDto) {
        return true;
    }

    @Override
    public NotificationChannel getNotificationChannel() {
        return NotificationChannel.EMAIL;
    }
}
