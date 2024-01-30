package com.ubb.taskmgmtservice.repository;

import com.ubb.taskmgmtservice.common.dto.task.TaskFilterRequestDto;
import com.ubb.taskmgmtservice.model.TaskDocument;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class CustomTaskRepositoryImpl implements CustomTaskRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<TaskDocument> filterTasks(TaskFilterRequestDto filterRequestDto) {
        // build filters
        final List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("userId").is(filterRequestDto.getUserId()));

        if (Objects.nonNull(filterRequestDto.getPriority())) {
            criterias.add(
                    Criteria.where("priority")
                            .is(filterRequestDto.getPriority())
            );
        }

        if (Objects.nonNull(filterRequestDto.getStatus())) {
            criterias.add(
                    Criteria.where("status")
                            .is(filterRequestDto.getStatus())
            );
        }

        if (StringUtils.isNotEmpty(filterRequestDto.getName())) {
            criterias.add(
                    Criteria.where("name").regex(filterRequestDto.getName(), "i")
            );
        }

        // build query
        final var query = new Query();
        var criteria = new Criteria().andOperator(criterias);
        query.addCriteria(criteria);

        // build page request
        final var pageable = PageRequest.of(filterRequestDto.getPage(), filterRequestDto.getPageSize());

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query.with(pageable), TaskDocument.class),
                pageable,
                () -> mongoTemplate.count(query, TaskDocument.class));
    }
}
