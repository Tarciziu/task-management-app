package com.ubb.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI config(@Value("${app.gateway-api-prefix}") final String gatewayApiPrefix,
                          @Value("${server.servlet.context-path}") final String contextPath) {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(
                                StringUtils.isEmpty(gatewayApiPrefix)
                                        ? contextPath
                                        : String.format("%s%s", gatewayApiPrefix, contextPath)
                        ));
    }
}
