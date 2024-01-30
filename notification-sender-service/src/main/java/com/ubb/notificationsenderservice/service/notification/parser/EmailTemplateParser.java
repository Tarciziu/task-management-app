package com.ubb.notificationsenderservice.service.notification.parser;

import com.ubb.notificationsenderservice.common.dto.EmailParsedContent;
import com.ubb.notificationsenderservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationsenderservice.common.enums.NotificationType;

import java.util.Optional;

public interface EmailTemplateParser {
    Optional<EmailParsedContent> parseTemplate(final NotificationType notificationType, final NotificationProcessorReceiverDto notificationProcessorReceiverDto);
}
