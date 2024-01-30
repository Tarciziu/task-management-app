package com.ubb.taskmgmtservice.service.auth;

import com.ubb.taskmgmtservice.common.security.AppUserPrincipal;
import com.ubb.taskmgmtservice.repository.UserRepository;
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
        return userRepository.findById(username)
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
