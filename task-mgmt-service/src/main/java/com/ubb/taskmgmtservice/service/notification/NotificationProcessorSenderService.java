package com.ubb.taskmgmtservice.service.notification;

public interface NotificationProcessorSenderService {
    <T> void sendMessage(final String queue, final T message);
}
