package com.ubb.userservice.service.usersettings;

import com.ubb.userservice.common.dto.usersettings.UserSettingsDto;
import com.ubb.userservice.common.enums.ErrorCode;
import com.ubb.userservice.common.enums.NotificationChannel;
import com.ubb.userservice.common.enums.NotificationType;
import com.ubb.userservice.common.security.AppUserPrincipal;
import com.ubb.userservice.config.exception.NotFoundException;
import com.ubb.userservice.mapper.UserSettingsMapper;
import com.ubb.userservice.model.UserSettingsDocument;
import com.ubb.userservice.repository.UserSettingsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {
    private UserSettingsRepository userSettingsRepository;
    private UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettingsDto createOrUpdateUserSettings(AppUserPrincipal appUserPrincipal, UserSettingsDto userSettingsDto) {
        final var currentUserSettings = getUserSettingsDocument(appUserPrincipal);
        userSettingsMapper.fromUserSettingsDto(currentUserSettings, userSettingsDto);
        userSettingsRepository.save(currentUserSettings);

        return userSettingsDto;
    }

    @Override
    public UserSettingsDto getUserSettings(AppUserPrincipal appUserPrincipal) {
        final var currentUserSettingsDocument = getUserSettingsDocument(appUserPrincipal);
        return userSettingsMapper.toUserSettingsDto(currentUserSettingsDocument);
    }

    @Override
    public void createDefaultUserSettingsForUser(String userId) {
        final var notificationsSettings = Arrays.stream(NotificationType.values())
                .filter(NotificationType::isEditable)
                .collect(Collectors.toMap(
                        value -> value,
                        value -> Arrays.stream(NotificationChannel.values())
                                .map(notificationChannel -> {
                                    var notificationTypeStatus = new UserSettingsDocument.NotificationTypeStatus();
                                    notificationTypeStatus.setChannel(notificationChannel);
                                    notificationTypeStatus.setEnabled(true);

                                    return notificationTypeStatus;
                                })
                                .collect(Collectors.toList())
                ));

        final var userSettingsDocument = new UserSettingsDocument();
        userSettingsDocument.setUserId(userId);
        userSettingsDocument.setNotifications(notificationsSettings);

        userSettingsRepository.save(userSettingsDocument);
    }

    private UserSettingsDocument getUserSettingsDocument(AppUserPrincipal appUserPrincipal) {
        return userSettingsRepository.findByUserId(appUserPrincipal.getId())
                .orElseThrow(() -> {
                    log.error("No settings notification found for user: {}", appUserPrincipal.getId());
                    return new NotFoundException(ErrorCode.USER_SETTINGS_NOT_FOUND);
                });
    }
}
