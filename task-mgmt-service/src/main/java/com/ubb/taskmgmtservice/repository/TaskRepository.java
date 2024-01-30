package com.ubb.taskmgmtservice.repository;

import com.ubb.taskmgmtservice.model.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskDocument, String>, CustomTaskRepository {

}
