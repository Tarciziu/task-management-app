package com.ubb.taskmgmtservice.controller;

import com.ubb.taskmgmtservice.common.dto.PagedResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskAddUpdateRequestDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskDetailsResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskFilterRequestDto;
import com.ubb.taskmgmtservice.common.enums.TaskPriority;
import com.ubb.taskmgmtservice.common.enums.TaskStatus;
import com.ubb.taskmgmtservice.service.auth.AppUserPrincipalService;
import com.ubb.taskmgmtservice.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
@Validated
public class TaskController {
    private final AppUserPrincipalService appUserPrincipalService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> addTask(@Valid @RequestBody final TaskAddUpdateRequestDto requestDto) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        final var taskId = taskService.addTask(currentUser, requestDto);

        final var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskId)
                .toUri();

        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<TaskDetailsResponseDto>> filterTasks(
            @RequestParam(name = "name", required = false) final String name,
            @RequestParam(name = "priority", required = false) final TaskPriority priority,
            @RequestParam(name = "status", required = false) final TaskStatus status,
            @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") final Integer pageSize
    ) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        final var pagedResponse = taskService.filterTasks(currentUser,
                TaskFilterRequestDto.builder()
                        .name(name)
                        .priority(priority)
                        .status(status)
                        .page(page)
                        .pageSize(pageSize)
                        .userId(currentUser.getId())
                        .build());

        return ResponseEntity.ok(pagedResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsResponseDto> getTaskById(@PathVariable("id") final String id) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        final var taskDetails = taskService.getTaskById(currentUser, id);

        return ResponseEntity.ok(taskDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDetailsResponseDto> updateTaskById(@PathVariable("id") final String id,
                                                                 @Validated @RequestBody final TaskAddUpdateRequestDto requestDto) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        final var taskDetails = taskService.updateTaskById(currentUser, id, requestDto);

        return ResponseEntity.ok(taskDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable("id") final String id) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        taskService.deleteTaskById(currentUser, id);

        return ResponseEntity.noContent().build();
    }
}
