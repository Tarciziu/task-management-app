package com.ubb.notificationsenderservice.service.notification.parser;

import com.ubb.notificationsenderservice.common.dto.EmailParsedContent;
import com.ubb.notificationsenderservice.common.dto.NotificationProcessorReceiverDto;
import com.ubb.notificationsenderservice.common.enums.NotificationType;
import com.ubb.notificationsenderservice.config.properties.TemplatesProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
public class EmailTemplateParserImpl implements EmailTemplateParser {
    private static final Pattern PLACEHOLDERS_REGEX = Pattern.compile("#\\{([^}]+)}");;
    private final TemplatesProperties templatesProperties;

    @Override
    public Optional<EmailParsedContent> parseTemplate(NotificationType notificationType, NotificationProcessorReceiverDto notificationProcessorReceiverDto) {
        final var emailTemplateDetails = templatesProperties.getEmail().get(notificationType);
        EmailParsedContent result = null;

        if (Objects.isNull(emailTemplateDetails)) {
            log.error("No template found for {}.", notificationType);
        } else {
            final var parsedSubject = parseContent(emailTemplateDetails.getSubject(), notificationProcessorReceiverDto);
            final var parsedContent = parseContent(emailTemplateDetails.getContent(), notificationProcessorReceiverDto);

            if (parsedSubject.isPresent() && parsedContent.isPresent()) {
                final var email = notificationProcessorReceiverDto.getDetails().stream()
                        .filter(pair -> pair.getKey().equals("email"))
                        .map(NotificationProcessorReceiverDto.NotificationDetailsItemDto::getValue)
                        .findFirst();

                if (email.isPresent()) {
                    result = EmailParsedContent.builder()
                            .to(email.get())
                            .subject(parsedSubject.get())
                            .content(parsedContent.get())
                            .build();
                }
            }
        }

        return Optional.ofNullable(result);
    }

    private Optional<String> parseContent(final String input, final NotificationProcessorReceiverDto notificationProcessorReceiverDto) {
        // Use regular expression to find placeholders in the template
        final var matcher = PLACEHOLDERS_REGEX.matcher(input);
        var result = input;

        // Iterate through placeholders and replace them with values
        while (matcher.find()) {
            final var placeholder = matcher.group(0); // #{key}
            final var key = matcher.group(1); // key without #{}

            final var value = notificationProcessorReceiverDto.getDetails()
                    .stream()
                    .filter(pair -> pair.getKey().equals(key))
                    .map(NotificationProcessorReceiverDto.NotificationDetailsItemDto::getValue)
                    .findFirst();

            if (value.isEmpty()) {
                log.error("No value provided for key: {}.", key);
                return Optional.empty();
            } else {
                result = result.replaceFirst(Pattern.quote(placeholder), Matcher.quoteReplacement(value.get()));
            }
        }
        return Optional.of(result);
    }
}
