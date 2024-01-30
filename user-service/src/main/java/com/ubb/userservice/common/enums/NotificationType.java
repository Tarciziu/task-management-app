package com.ubb.userservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    TASK_ADDED(true),
    TASK_UPDATED(true),
    TASK_DELETED(true),

    // should not be added to user settings
    WELCOME(false);

    private final boolean editable;
}
