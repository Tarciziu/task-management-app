package com.ubb.userservice.repository;

import com.ubb.userservice.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(final String email);
    boolean existsByEmail(final String email);
}
