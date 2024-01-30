package com.ubbsoftware.todogatewayservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    private String sessionValidationUri;
}
