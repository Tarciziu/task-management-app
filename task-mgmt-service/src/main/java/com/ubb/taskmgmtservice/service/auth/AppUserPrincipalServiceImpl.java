package com.ubb.taskmgmtservice.service.auth;

import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import com.ubb.taskmgmtservice.common.security.AppUserPrincipal;
import com.ubb.taskmgmtservice.config.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AppUserPrincipalServiceImpl implements AppUserPrincipalService {
    @Override
    public Optional<AppUserPrincipal> getAppUserPrincipal() {
        if (Objects.nonNull(SecurityContextHolder.getContext()) && Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
                && Objects.nonNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal()) &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AppUserPrincipal) {
            return Optional.of((AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        return Optional.empty();
    }

    @Override
    public AppUserPrincipal getAppUserPrincipalOrThrow() throws UnAuthorizedException {
        return this.getAppUserPrincipal()
                .orElseThrow(() -> {
                    log.error("Error while getting the user from session");
                    return new UnAuthorizedException(ErrorCode.UNAUTHORIZED);
                });
    }
}
