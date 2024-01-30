package com.ubb.taskmgmtservice.model;

import com.ubb.taskmgmtservice.common.enums.TaskPriority;
import com.ubb.taskmgmtservice.common.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Document("tasks")
public class TaskDocument extends BaseObject {
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime deadline;
    private String userId;
}
