package com.ubb.userservice.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.userservice.common.constants.AppConstants;
import com.ubb.userservice.common.dto.ErrorResponseDto;
import com.ubb.userservice.common.enums.ErrorCode;
import com.ubb.userservice.config.security.matcher.SkipPathRequestAntMatcher;
import com.ubb.userservice.service.auth.JwtMakerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Order(1)
public class JwtFilter extends OncePerRequestFilter {
    private final JwtMakerService jwtMakerService;
    private final SkipPathRequestAntMatcher skipPathRequestAntMatcher;
    private final ObjectMapper objectMapper;
    private final UserDetailsService jwtUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (skipPathRequestAntMatcher.matches(request)) {
            doFilter(request, response, filterChain);
            return;
        }

        var tokenFromCookieOptional = getAccessTokenFromCookie(request);
        if (tokenFromCookieOptional.isEmpty()) {
            log.error("Token not found in cookie.");
            onFailure(response, ErrorCode.UNAUTHORIZED);
            return;
        }

        final var accessTokenValue = tokenFromCookieOptional.get();
        final var isValid = jwtMakerService.validateAccessToken(accessTokenValue);
        if (!isValid) {
            log.error("Jwt not valid.");
            onFailure(response, ErrorCode.UNAUTHORIZED);
            return;
        }

        final var authentication = jwtUserService.loadUserByUsername(jwtMakerService.getEmail(accessTokenValue).orElse(""));

        try {
            setAuthenticationContext(authentication, request);
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private Optional<String> getAccessTokenFromCookie(final HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                .orElse(new Cookie[]{}))
                .filter(cookie -> cookie.getName().equals(AppConstants.AUTH_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue);
    }

    /**
     * Sets details of the user making the request containing an authorization header
     *
     * @param userDetails user details used to set the authentication context
     * @param request     the current request
     */
    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void onFailure(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var errorMessage = new ErrorResponseDto();
        errorMessage.setMessage(errorCode.getMessage());
        errorMessage.setCode(errorMessage.getCode());
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());

        var body = objectMapper.writeValueAsBytes(errorMessage);
        response.getOutputStream().write(body);
    }
}
