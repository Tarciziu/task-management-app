package com.ubb.notificationsenderservice.service.notification.email;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String emailContent);
}
