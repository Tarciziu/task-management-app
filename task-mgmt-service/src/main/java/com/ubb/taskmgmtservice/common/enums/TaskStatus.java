package com.ubb.taskmgmtservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String value;
}
