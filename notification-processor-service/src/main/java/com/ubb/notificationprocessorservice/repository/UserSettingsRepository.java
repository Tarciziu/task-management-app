package com.ubb.notificationprocessorservice.repository;

import com.ubb.notificationprocessorservice.model.UserSettingsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSettingsRepository extends MongoRepository<UserSettingsDocument, String> {
    Optional<UserSettingsDocument> findByUserId(final String userId);
}
