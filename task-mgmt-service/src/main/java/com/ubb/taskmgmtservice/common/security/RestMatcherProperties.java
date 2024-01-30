package com.ubb.taskmgmtservice.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Setter
@Getter
public class RestMatcherProperties {
    private HttpMethod method;
    private List<String> uris;

    public boolean hasMethod() {
        return Objects.nonNull(method);
    }

    public RequestMatcher toRequestMatcher() {
        final List<RequestMatcher> requestMatchers = uris.stream()
                .map((uri) -> {
                    if (hasMethod()) {
                        return new AntPathRequestMatcher(uri, method.name());
                    } else {
                        return new AntPathRequestMatcher(uri);
                    }
                })
                .collect(toList());

        return new OrRequestMatcher(requestMatchers);
    }
}
