package com.ubb.notificationsenderservice.service.notification.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

@AllArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final String from;

    @Override
    public void sendEmail(String to, String subject, String emailContent) {
        // we use a different thread not to block the current request
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(emailContent);
        };

        javaMailSender.send(messagePreparator);
    }
}
