package com.ubb.userservice.service.auth;

import com.ubb.userservice.common.dto.auth.AuthRequestDto;
import com.ubb.userservice.common.dto.auth.CurrentUserResponseDto;
import com.ubb.userservice.common.dto.auth.SignInRequestDto;
import com.ubb.userservice.common.security.AppUserPrincipal;

public interface AuthService {
    String login(final AuthRequestDto authRequestDto);
    CurrentUserResponseDto getCurrentUser(final AppUserPrincipal appUserPrincipal);
    void signIn(final SignInRequestDto signInRequestDto);
}
