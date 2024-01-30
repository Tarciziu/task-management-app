package com.ubb.userservice.service.notification;

public interface NotificationProcessorSenderService {
    <T> void sendMessage(final String queue, final T message);
}
