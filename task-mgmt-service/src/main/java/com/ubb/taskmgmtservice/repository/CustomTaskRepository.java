package com.ubb.taskmgmtservice.repository;

import com.ubb.taskmgmtservice.common.dto.task.TaskFilterRequestDto;
import com.ubb.taskmgmtservice.model.TaskDocument;
import org.springframework.data.domain.Page;

public interface CustomTaskRepository {
    Page<TaskDocument> filterTasks(final TaskFilterRequestDto filterRequestDto);
}
