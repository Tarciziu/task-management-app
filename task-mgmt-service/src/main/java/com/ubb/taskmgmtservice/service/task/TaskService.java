package com.ubb.taskmgmtservice.service.task;

import com.ubb.taskmgmtservice.common.dto.PagedResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskAddUpdateRequestDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskDetailsResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskFilterRequestDto;
import com.ubb.taskmgmtservice.common.security.AppUserPrincipal;

public interface TaskService {
    String addTask(final AppUserPrincipal currentUser, final TaskAddUpdateRequestDto taskAddUpdateRequestDto);
    TaskDetailsResponseDto getTaskById(final AppUserPrincipal currentUser, final String id);
    void deleteTaskById(final AppUserPrincipal currentUser, final String id);
    TaskDetailsResponseDto updateTaskById(final AppUserPrincipal currentUser, final String id, final TaskAddUpdateRequestDto taskAddUpdateRequestDto);

    PagedResponseDto<TaskDetailsResponseDto> filterTasks(AppUserPrincipal currentUser, TaskFilterRequestDto build);
}
