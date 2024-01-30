package com.ubb.userservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document("users")
public class UserDocument {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<RoleItem> roles;

    public enum RoleItem {
        ADMIN,
        USER
    }
}
