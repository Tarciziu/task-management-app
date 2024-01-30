package com.ubb.taskmgmtservice.common.dto.task;

import com.ubb.taskmgmtservice.common.enums.TaskPriority;
import com.ubb.taskmgmtservice.common.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaskFilterRequestDto {
    private TaskPriority priority;
    private TaskStatus status;
    private String name;

    private String userId;

    // pagination
    private Integer page;
    private Integer pageSize;
}
