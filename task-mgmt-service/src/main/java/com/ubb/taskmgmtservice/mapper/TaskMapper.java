package com.ubb.taskmgmtservice.mapper;

import com.ubb.taskmgmtservice.common.dto.PagedResponseDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskAddUpdateRequestDto;
import com.ubb.taskmgmtservice.common.dto.task.TaskDetailsResponseDto;
import com.ubb.taskmgmtservice.model.TaskDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskDocument fromTaskAddRequestDto(final TaskAddUpdateRequestDto requestDto);
    TaskDetailsResponseDto fromTaskDocument(final TaskDocument taskDocument);
    void fromTaskUpdateRequestDto(@MappingTarget TaskDocument taskDocument, final TaskAddUpdateRequestDto requestDto);

    default PagedResponseDto<TaskDetailsResponseDto> fromPage(final Page<TaskDocument> taskDocumentPage) {
        final var metadata = new PagedResponseDto.PagingMetadata();
        metadata.setTotalPages(taskDocumentPage.getTotalPages());
        metadata.setTotalItems(taskDocumentPage.getTotalElements());

        final PagedResponseDto<TaskDetailsResponseDto> pagedResponse = new PagedResponseDto<>();
        pagedResponse.setData(
                taskDocumentPage.getContent()
                        .stream()
                        .map(this::fromTaskDocument)
                        .toList()
        );
        pagedResponse.setMetadata(metadata);

        return pagedResponse;
    }
}
