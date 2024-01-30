package com.ubb.userservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED("E1001", "Unauthorized"),
    EMAIL_IN_USE("E1002", "Email already exists"),
    USER_SETTINGS_NOT_FOUND("E1003", "User preferences not found"),

    INTERNAL_SERVER_ERROR("E2000", "Internal server error"),
    BAD_REQUEST("E2001", "Invalid Data");;

    private final String code;
    private final String message;
}
