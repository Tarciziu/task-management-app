package com.ubb.userservice.service.auth;

import com.ubb.userservice.common.security.AppUserPrincipal;
import com.ubb.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(userDocument -> AppUserPrincipal.builder()
                        .id(userDocument.getId())
                        .firstName(userDocument.getFirstName())
                        .lastName(userDocument.getLastName())
                        .email(userDocument.getEmail())
                        .password(userDocument.getPassword())
                        .roles(userDocument.getRoles())
                        .build())
                .orElseThrow(() -> {
                   log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }
}
