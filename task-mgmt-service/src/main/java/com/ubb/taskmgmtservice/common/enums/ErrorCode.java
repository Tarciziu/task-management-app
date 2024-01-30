package com.ubb.taskmgmtservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED("E1001", "Unauthorized"),

    // tasks
    TASK_NOT_FOUND("E1051", "Task not found"),

    INTERNAL_SERVER_ERROR("E2000", "Internal server error"),
    BAD_REQUEST("E2001", "Invalid Data");

    private final String code;
    private final String message;
}
