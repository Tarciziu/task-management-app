package com.ubb.userservice.service.usersettings;

import com.ubb.userservice.common.dto.usersettings.UserSettingsDto;
import com.ubb.userservice.common.security.AppUserPrincipal;

public interface UserSettingsService {
    UserSettingsDto createOrUpdateUserSettings(final AppUserPrincipal appUserPrincipal, final UserSettingsDto userSettingsDto);
    UserSettingsDto getUserSettings(final AppUserPrincipal appUserPrincipal);

    void createDefaultUserSettingsForUser(final String userId);
}
