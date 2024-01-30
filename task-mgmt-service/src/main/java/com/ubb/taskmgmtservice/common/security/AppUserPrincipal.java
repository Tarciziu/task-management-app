package com.ubb.taskmgmtservice.common.security;

import com.ubb.taskmgmtservice.model.UserDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
public class AppUserPrincipal implements UserDetails {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<UserDocument.RoleItem> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return buildSimpleGrantedAuthorities(roles);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<UserDocument.RoleItem> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
}
