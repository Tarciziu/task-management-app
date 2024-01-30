package com.ubb.userservice.service.auth;

import com.ubb.userservice.common.security.AppUserPrincipal;
import com.ubb.userservice.config.exception.UnAuthorizedException;

import java.util.Optional;

public interface AppUserPrincipalService {
    Optional<AppUserPrincipal> getAppUserPrincipal();

    AppUserPrincipal getAppUserPrincipalOrThrow();
}
