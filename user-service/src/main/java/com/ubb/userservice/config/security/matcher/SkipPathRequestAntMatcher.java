package com.ubb.userservice.config.security.matcher;

import com.ubb.userservice.common.security.RestMatcherProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class SkipPathRequestAntMatcher implements RequestMatcher {
    private final OrRequestMatcher skipRequestMatcher;
    private final RequestMatcher processingPathMatcher;

    public SkipPathRequestAntMatcher(List<RestMatcherProperties> skipRequestMatchers, String processingPath) {
        this.skipRequestMatcher = new OrRequestMatcher(
                skipRequestMatchers
                        .stream()
                        .map(RestMatcherProperties::toRequestMatcher)
                        .toList()
        );
        this.processingPathMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (skipRequestMatcher.matches(request)) {
            return true;
        }
        return processingPathMatcher.matches(request);
    }
}

