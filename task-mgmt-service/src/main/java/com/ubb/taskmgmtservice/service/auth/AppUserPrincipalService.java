package com.ubb.taskmgmtservice.service.auth;

import com.ubb.taskmgmtservice.common.security.AppUserPrincipal;

import java.util.Optional;

public interface AppUserPrincipalService {
    Optional<AppUserPrincipal> getAppUserPrincipal();

    AppUserPrincipal getAppUserPrincipalOrThrow();
}
