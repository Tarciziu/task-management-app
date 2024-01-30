package com.ubb.taskmgmtservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("add-user-id-header")
                .addOperationCustomizer((operation, $) -> {
                    operation.addParametersItem(
                            new HeaderParameter()
                                    .name("x-user-id")
                                    .description("Unique user id")
                                    .required(true)
                    );
                    return operation;
                })
                .build();
    }

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
