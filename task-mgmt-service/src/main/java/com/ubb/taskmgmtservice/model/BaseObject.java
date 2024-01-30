package com.ubb.taskmgmtservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Setter
@Getter
public abstract class BaseObject {

    @Id
    protected String id;

    @Indexed
    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
