package com.ubbsoftware.todogatewayservice.filters;

import com.ubbsoftware.todogatewayservice.common.dto.SessionValidationResponseDto;
import com.ubbsoftware.todogatewayservice.config.properties.AuthProperties;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<Object> {
    private static final String X_USER_ID_HEADER_NAME = "X-User-Id";
    private static final List<HttpStatusCode> ALLOWED_STATUSES = Arrays.asList(
            HttpStatus.OK,
            HttpStatus.UNAUTHORIZED
    );

    private final AuthProperties authProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            final var request = exchange.getRequest();

            if (request.getURI().toString().contains("/v3/api-docs/")) {
                return chain.filter(exchange);
            }

            final var webClient = WebClient.create(authProperties.getSessionValidationUri());
            return webClient
                    .method(HttpMethod.GET)
                    .headers(headers -> headers.putAll(request.getHeaders()))
                    .cookies(stringStringMultiValueMap -> request.getCookies().forEach((key, value) -> {
                        stringStringMultiValueMap.put(
                                key,
                                value.stream()
                                        .map(HttpCookie::getValue)
                                        .toList()
                        );
                    }))
                    .exchangeToMono(response -> {
                        if (ALLOWED_STATUSES.contains(response.statusCode())) {
                            // nothing to do
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToMono(SessionValidationResponseDto.class)
                                        .flatMap(body -> {
                                            exchange.getRequest()
                                                    .mutate()
                                                    .header(X_USER_ID_HEADER_NAME, body.getUserId());
                                            return chain.filter(exchange);
                                        });
                            }

                            return chain.filter(exchange);
                        } else {
                            exchange.getResponse().setStatusCode(response.statusCode());
                            exchange.getResponse().getHeaders().putAll(response.headers().asHttpHeaders());
                            return response.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        final var buffer = exchange.getResponse()
                                                .bufferFactory()
                                                .wrap(body.getBytes());
                                        return exchange.getResponse().writeWith(Mono.just(buffer));
                                    });
                        }
                    });
        };
    }
}
