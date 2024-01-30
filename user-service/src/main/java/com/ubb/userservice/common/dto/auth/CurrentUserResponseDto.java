package com.ubb.userservice.common.dto.auth;

import com.ubb.userservice.model.UserDocument;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class CurrentUserResponseDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserDocument.RoleItem> roles;
}
