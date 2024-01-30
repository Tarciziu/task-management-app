package com.ubb.taskmgmtservice.common.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDetailsResponseDto extends TaskAddUpdateRequestDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
