package com.ubb.taskmgmtservice.common.dto.task;

import com.ubb.taskmgmtservice.common.enums.TaskPriority;
import com.ubb.taskmgmtservice.common.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskAddUpdateRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private TaskStatus status;

    private LocalDateTime deadline;
}
