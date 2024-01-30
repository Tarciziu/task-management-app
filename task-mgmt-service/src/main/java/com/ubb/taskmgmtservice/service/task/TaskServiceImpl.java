package com.ubb.taskmgmtservice.service.task;

import com.ubb.taskmgmtservice.common.dto.NotificationProcessorSenderRequestDto;
import com.ubb.taskmgmtservice.common.dto.PagedResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskAddUpdateRequestDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskDetailsResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskFilterRequestDto;
import com.ubb.taskmgmtservice.common.enums.NotificationType;
import com.ubb.taskmgmtservice.common.security.AppUserPrincipal;
import com.ubb.taskmgmtservice.config.exception.NotFoundException;
import com.ubb.taskmgmtservice.config.properties.MessagingProperties;
import com.ubb.taskmgmtservice.mapper.TaskMapper;
import com.ubb.taskmgmtservice.model.TaskDocument;
import com.ubb.taskmgmtservice.repository.TaskRepository;
import com.ubb.taskmgmtservice.service.notification.NotificationProcessorSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;

import static com.ubb.taskmgmtservice.common.enums.ErrorCode.TASK_NOT_FOUND;

@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final MessagingProperties messagingProperties;
    private final NotificationProcessorSenderService notificationProcessorSenderService;

    @Override
    public String addTask(AppUserPrincipal currentUser, TaskAddUpdateRequestDto taskAddUpdateRequestDto) {
        final var taskDocument = taskMapper.fromTaskAddRequestDto(taskAddUpdateRequestDto);
        taskDocument.setUserId(currentUser.getId());
        final var addedTask = taskRepository.save(taskDocument);

        final var notificationData = NotificationProcessorSenderRequestDto.builder()
                .notificationType(NotificationType.TASK_ADDED)
                .userId(currentUser.getId())
                .details(Arrays.asList(
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("name")
                                .value(taskDocument.getName())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("description")
                                .value(taskDocument.getDescription())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("priority")
                                .value(taskDocument.getPriority().name())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("email")
                                .value(currentUser.getEmail())
                                .build()
                ))
                .build();
        this.notificationProcessorSenderService.sendMessage(messagingProperties.getQueues().getNotifications(), notificationData);

        return addedTask.getId();
    }

    @Override
    public TaskDetailsResponseDto getTaskById(AppUserPrincipal currentUser, String id) {
        final var taskDocument = getTaskDocumentAndValidateOwnership(currentUser, id);
        return taskMapper.fromTaskDocument(taskDocument);
    }

    @Override
    public void deleteTaskById(AppUserPrincipal currentUser, String id) {
        final var taskDocument = getTaskDocumentAndValidateOwnership(currentUser, id);
        taskRepository.deleteById(taskDocument.getId());

        final var notificationData = NotificationProcessorSenderRequestDto.builder()
                .notificationType(NotificationType.TASK_DELETED)
                .userId(currentUser.getId())
                .details(Arrays.asList(
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("name")
                                .value(taskDocument.getName())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("email")
                                .value(currentUser.getEmail())
                                .build()
                ))
                .build();
        this.notificationProcessorSenderService.sendMessage(messagingProperties.getQueues().getNotifications(), notificationData);
    }

    @Override
    public TaskDetailsResponseDto updateTaskById(AppUserPrincipal currentUser, String id, TaskAddUpdateRequestDto taskAddUpdateRequestDto) {
        final var taskDocument = getTaskDocumentAndValidateOwnership(currentUser, id);
        taskMapper.fromTaskUpdateRequestDto(taskDocument, taskAddUpdateRequestDto);
        final var updatedTaskDocument = taskRepository.save(taskDocument);
        final var updatedTask = taskMapper.fromTaskDocument(updatedTaskDocument);

        final var notificationData = NotificationProcessorSenderRequestDto.builder()
                .notificationType(NotificationType.TASK_UPDATED)
                .userId(currentUser.getId())
                .details(Arrays.asList(
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("name")
                                .value(taskDocument.getName())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("description")
                                .value(taskDocument.getDescription())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("priority")
                                .value(taskDocument.getPriority().name())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("status")
                                .value(taskDocument.getStatus().getValue())
                                .build(),
                        NotificationProcessorSenderRequestDto.NotificationDetailsItemDto.builder()
                                .key("email")
                                .value(currentUser.getEmail())
                                .build()
                ))
                .build();
        this.notificationProcessorSenderService.sendMessage(messagingProperties.getQueues().getNotifications(), notificationData);

        return updatedTask;
    }

    @Override
    public PagedResponseDto<TaskDetailsResponseDto> filterTasks(AppUserPrincipal currentUser, TaskFilterRequestDto filterTask) {
        final var pagedResponse = taskRepository.filterTasks(filterTask);
        return taskMapper.fromPage(pagedResponse);
    }

    private TaskDocument getTaskDocumentAndValidateOwnership(AppUserPrincipal currentUser, String id) {
        final var taskDocument = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No task found with id: {}", id);
                    return new NotFoundException(TASK_NOT_FOUND);
                });

        if (!taskDocument.getUserId().equals(currentUser.getId())) {
            log.error("Task does not belong to the current user");
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        return taskDocument;
    }
}
