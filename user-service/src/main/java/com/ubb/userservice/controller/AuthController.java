package com.ubb.userservice.controller;

import com.ubb.userservice.common.constants.AppConstants;
import com.ubb.userservice.common.dto.auth.AuthRequestDto;
import com.ubb.userservice.common.dto.auth.CurrentUserResponseDto;
import com.ubb.userservice.common.dto.auth.SessionValidationResponseDto;
import com.ubb.userservice.common.dto.auth.SignInRequestDto;
import com.ubb.userservice.config.properties.JwtProperties;
import com.ubb.userservice.service.auth.AppUserPrincipalService;
import com.ubb.userservice.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProperties authProperties;
    private final AppUserPrincipalService appUserPrincipalService;

    @Value("${app.gateway-api-prefix}")
    private String apiGatewayPrefix;

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody final SignInRequestDto signInRequestDto) {
        authService.signIn(signInRequestDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final AuthRequestDto authRequestDto) {
        final var jwt = authService.login(authRequestDto);

        final var authCookie = ResponseCookie.from(AppConstants.AUTH_COOKIE_NAME, jwt)
                .path(getCookiePath())
                .secure(false) // theoretically with HTTPS it should be true
                .httpOnly(false)
                .maxAge(authProperties.getExpiryInSeconds())
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        final var authCookie = ResponseCookie.from(AppConstants.AUTH_COOKIE_NAME, "")
                .path(getCookiePath())
                .secure(false) // theoretically with HTTPS it should be true
                .httpOnly(false)
                .maxAge(-1)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDto> getCurrentUser() {
        final var currentUser = authService.getCurrentUser(appUserPrincipalService.getAppUserPrincipalOrThrow());

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/session-validation")
    public ResponseEntity<SessionValidationResponseDto> checkSessionValidation() {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        return ResponseEntity.ok(
                SessionValidationResponseDto.builder()
                        .userId(currentUser.getId())
                        .build()
        );
    }

    private String getCookiePath() {
        return StringUtils.isEmpty(apiGatewayPrefix) ? "/" : apiGatewayPrefix;
    }
}
