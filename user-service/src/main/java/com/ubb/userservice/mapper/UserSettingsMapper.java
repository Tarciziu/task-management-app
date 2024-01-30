package com.ubb.userservice.mapper;


import com.ubb.userservice.common.dto.usersettings.UserSettingsDto;
import com.ubb.userservice.model.UserSettingsDocument;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserSettingsMapper {
    default void fromUserSettingsDto(UserSettingsDocument userSettingsDocument, UserSettingsDto userSettingsDto) {
        var mappedNotifications = userSettingsDto.getNotifications()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(value -> {
                                    var notificationTypeStatus = new UserSettingsDocument.NotificationTypeStatus();
                                    notificationTypeStatus.setEnabled(value.isEnabled());
                                    notificationTypeStatus.setChannel(value.getChannel());

                                    return notificationTypeStatus;
                                })
                                .collect(Collectors.toList())
                ));

        userSettingsDocument.setNotifications(mappedNotifications);
    }

    default UserSettingsDto toUserSettingsDto(UserSettingsDocument userSettingsDocument) {
        var mappedNotifications = userSettingsDocument.getNotifications()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(value -> {
                                    var notificationTypeStatus = new UserSettingsDto.NotificationTypeStatus();
                                    notificationTypeStatus.setEnabled(value.isEnabled());
                                    notificationTypeStatus.setChannel(value.getChannel());

                                    return notificationTypeStatus;
                                })
                                .collect(Collectors.toList())
                ));
        var userSettingsDto = new UserSettingsDto();
        userSettingsDto.setNotifications(mappedNotifications);

        return userSettingsDto;
    }
}
