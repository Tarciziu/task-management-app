package com.ubb.taskmgmtservice.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.taskmgmtservice.common.constants.AppConstants;
import com.ubb.taskmgmtservice.common.dto.ErrorResponseDto;
import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import com.ubb.taskmgmtservice.config.security.matcher.SkipPathRequestAntMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Order(1)
public class XUserIdFilter extends OncePerRequestFilter {
    private final SkipPathRequestAntMatcher skipPathRequestAntMatcher;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (skipPathRequestAntMatcher.matches(request)) {
            doFilter(request, response, filterChain);
            return;
        }

        var userIdFromHeaderOptional = getUserIdFromRequest(request);
        if (userIdFromHeaderOptional.isEmpty()) {
            log.error("X-User-Id header not found.");
            onFailure(response, ErrorCode.UNAUTHORIZED);
            return;
        }

        final var authentication = userDetailsService.loadUserByUsername(userIdFromHeaderOptional.get());

        try {
            setAuthenticationContext(authentication, request);
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private Optional<String> getUserIdFromRequest(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AppConstants.X_USER_ID_HEADER_NAME));
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
