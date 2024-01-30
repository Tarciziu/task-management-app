package com.ubb.userservice.config.properties;

import com.ubb.userservice.common.security.RestMatcherProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "app.auth.security.apis")
@Getter
@Setter
public class SecurityProperties {
    private List<RestMatcherProperties> publicEndpoints;
    private List<PrivateEndpointsDefinition> privateEndpoints;

    @Getter
    @Setter
    public static class PrivateEndpointsDefinition extends RestMatcherProperties {
        private List<String> roles;

        public boolean hasRoles() {
            return Objects.nonNull(roles);
        }
    }

}
