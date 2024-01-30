package com.ubb.userservice.mapper;

import com.ubb.userservice.common.dto.auth.CurrentUserResponseDto;
import com.ubb.userservice.common.dto.auth.SignInRequestDto;
import com.ubb.userservice.common.security.AppUserPrincipal;
import com.ubb.userservice.model.UserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDocument fromSignInRequestDto(final SignInRequestDto requestDto);

    CurrentUserResponseDto fromAppUserPrincipal(final AppUserPrincipal userPrincipal);
}
