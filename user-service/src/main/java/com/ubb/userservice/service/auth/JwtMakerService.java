package com.ubb.userservice.service.auth;

import java.util.Optional;

public interface JwtMakerService {
    String generateJwt(final String id, final String email);
    boolean validateAccessToken(String token);

    String getSubject(final String jwt);

    Optional<String> getUserId(final String jwt);

    Optional<String> getEmail(final String jwt);
}
