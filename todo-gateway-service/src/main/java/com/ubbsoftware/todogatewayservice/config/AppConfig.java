package com.ubbsoftware.todogatewayservice.config;

import com.ubbsoftware.todogatewayservice.config.properties.AuthProperties;
import com.ubbsoftware.todogatewayservice.filters.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AuthenticationFilter authenticationFilter(final AuthProperties authProperties) {
        return new AuthenticationFilter(authProperties);
    }

//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.addAllowedOrigin("*"); // Allow all origins
//        corsConfig.addAllowedMethod("*"); // Allow all methods (GET, POST, PUT, DELETE, etc.)
//        corsConfig.addAllowedHeader("*"); // Allow all headers
//        corsConfig.setMaxAge(3600L); // Cache preflight response for 1 hour
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig); // Apply the CORS configuration to all paths
//
//        return new CorsWebFilter(source);
//    }
}
