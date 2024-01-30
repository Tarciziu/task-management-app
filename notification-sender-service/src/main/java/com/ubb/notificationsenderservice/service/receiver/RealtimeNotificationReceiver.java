package com.ubb.notificationsenderservice.service.receiver;

import org.springframework.kafka.annotation.KafkaListener;

public class RealtimeNotificationReceiver {
    @KafkaListener(topics = "${app.messaging.topics.notifications.REALTIME}")
    public void receiveMessage(String message) {
        System.out.println("Received Message in group REALTIME: " + message);
    }
}
