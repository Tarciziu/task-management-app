package com.ubb.userservice.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ubb.userservice.config.properties.JwtProperties;
import com.ubb.userservice.service.datetime.DateTimeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;

import static com.ubb.userservice.common.constants.AppConstants.EMAIL_CLAIM_NAME;
import static com.ubb.userservice.common.constants.AppConstants.USER_ID_CLAIM_NAME;

@Slf4j
@AllArgsConstructor
public class JwtMakerServiceImpl implements JwtMakerService {
    private final JwtProperties authProperties;
    private final DateTimeService dateTimeService;
    private final JWTVerifier jwtVerifier;

    @Override
    public String generateJwt(String userId, String email) {
        final Instant now = dateTimeService.currentTime();

        return JWT.create()
                .withJWTId(userId)
                .withSubject(userId)
                .withClaim(USER_ID_CLAIM_NAME, userId)
                .withClaim(EMAIL_CLAIM_NAME, email)
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(authProperties.getExpiryInSeconds()))
                .sign(Algorithm.HMAC512(authProperties.getSecret()));
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error("{} {}", "JWT expired on" + e.getExpiredOn(), e.getMessage());
        } catch (InvalidClaimException e) {
            log.error("Claim is not valid " + e.getMessage());
        } catch (AlgorithmMismatchException e) {
            log.error("Jwt algorithm mismatch" + e.getMessage());
        } catch (JWTVerificationException e) {
            log.error("Jwt verification error " + e.getMessage());
        }
        return false;
    }

    @Override
    public String getSubject(String jwt) {
        return getDecodedJwt(jwt).getSubject();
    }

    @Override
    public Optional<String> getUserId(String jwt) {
        return getFromClaims(jwt, USER_ID_CLAIM_NAME, String.class);
    }

    @Override
    public Optional<String> getEmail(String jwt) {
        return getFromClaims(jwt, EMAIL_CLAIM_NAME, String.class);
    }

    private DecodedJWT getDecodedJwt(String token) {
        return JWT.decode(token);
    }

    private <T> Optional<T> getFromClaims(final String token, final String claimName, final Class<T> cast) {
        return Optional.ofNullable(getDecodedJwt(token).getClaims().get(claimName))
                .map(value -> value.as(cast));
    }
}
