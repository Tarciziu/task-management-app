package com.ubb.userservice.controller;

import com.ubb.userservice.common.dto.usersettings.UserSettingsDto;
import com.ubb.userservice.service.auth.AppUserPrincipalService;
import com.ubb.userservice.service.usersettings.UserSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-settings")
@AllArgsConstructor
public class UserSettingsController {
    private final AppUserPrincipalService appUserPrincipalService;
    private final UserSettingsService userSettingsService;

    // TODO: add custom validator for preferences
    @PutMapping
    public ResponseEntity<UserSettingsDto> createOrUpdateUserSettings(@RequestBody final UserSettingsDto userSettingsDto) {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        return ResponseEntity.ok(userSettingsService.createOrUpdateUserSettings(currentUser, userSettingsDto));
    }

    @GetMapping
    public ResponseEntity<UserSettingsDto> getUserSettings() {
        final var currentUser = appUserPrincipalService.getAppUserPrincipalOrThrow();
        return ResponseEntity.ok(userSettingsService.getUserSettings(currentUser));
    }
}
