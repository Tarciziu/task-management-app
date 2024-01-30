package com.ubb.notificationprocessorservice.service.sender;

public interface NotificationSenderService {
    <T> void sendNotification(final String topic, final T message);
}
