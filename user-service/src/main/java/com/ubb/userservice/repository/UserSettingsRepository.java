package com.ubb.userservice.repository;

import com.ubb.userservice.model.UserSettingsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSettingsRepository extends MongoRepository<UserSettingsDocument, String> {
    Optional<UserSettingsDocument> findByUserId(final String userId);
}
