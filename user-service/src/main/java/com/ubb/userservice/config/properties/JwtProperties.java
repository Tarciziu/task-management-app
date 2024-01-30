package com.ubb.userservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.auth.jwt")
public class JwtProperties {
    private String secret;
    private Long expiryInSeconds;
}
