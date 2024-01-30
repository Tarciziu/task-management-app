package com.ubb.taskmgmtservice.config.security;

import com.ubb.taskmgmtservice.config.properties.SecurityProperties;
import com.ubb.taskmgmtservice.config.security.filters.XUserIdFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final XUserIdFilter XUserIdFilter,
                                           final SecurityProperties securityProperties) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable())
                .authorizeHttpRequests(authorizeHttpRequests -> {
                    // private APIs
                    applyPrivateEndpointPermissions(securityProperties, authorizeHttpRequests);

                    // public APIs
                    applyPublicEndpointPermissions(securityProperties, authorizeHttpRequests);

                    // any request authenticated
                    authorizeHttpRequests.anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> {

                })
                .addFilterBefore(XUserIdFilter, UsernamePasswordAuthenticationFilter.class)
                .securityContext(securityContext -> securityContext.requireExplicitSave(true))
                .build();
    }

    private void applyPrivateEndpointPermissions(SecurityProperties securityProperties,
                                                 AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        if (Objects.isNull(securityProperties.getPrivateEndpoints())) {
            return;
        }
        securityProperties.getPrivateEndpoints().forEach((restrictions) -> {
            if (CollectionUtils.isEmpty(restrictions.getUris())) {
                return;
            }

            String[] uris = restrictions.getUris().toArray(new String[0]);
            if (restrictions.hasMethod()) {
                var configurator = auth.requestMatchers(restrictions.getMethod(), uris);
                if (restrictions.hasRoles()) {
                    String[] roles = restrictions.getRoles().toArray(new String[0]);
                    configurator.hasAnyAuthority(roles);
                } else {
                    configurator.authenticated();
                }
            } else {
                var configurator = auth.requestMatchers(uris);
                if (restrictions.hasRoles()) {
                    String[] roles = restrictions.getRoles().toArray(new String[0]);
                    configurator.hasAnyAuthority(roles);
                } else {
                    configurator.authenticated();
                }
            }
        });
    }

    private void applyPublicEndpointPermissions(SecurityProperties securityProperties,
                                                AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
                                                ) {
        if (Objects.isNull(securityProperties.getPublicEndpoints())) {
            return;
        }
        securityProperties.getPublicEndpoints().forEach(restrictions -> {
            String[] uris = restrictions.getUris().toArray(new String[0]);
            if (restrictions.hasMethod()) {
                auth.requestMatchers(restrictions.getMethod(), uris).permitAll();
            } else {
                auth.requestMatchers(uris).permitAll();
            }
        });
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
