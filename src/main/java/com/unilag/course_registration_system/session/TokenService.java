package com.unilag.course_registration_system.session;

import com.unilag.course_registration_system.dto.request.TokenRequest;
import com.unilag.course_registration_system.dto.response.TokenResponse;
import com.unilag.course_registration_system.dto.response.TokenValidationResponse;
import com.unilag.course_registration_system.entity.Student;
import com.unilag.course_registration_system.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JWTService jwtService;

    /**
     * Generate a session token for the given student data.
     */
    public TokenResponse generateToken(Student request) {


        String token = jwtService.generateToken(request);

        Instant issuedAt  = jwtService.getIssuedAt(token);
        Instant expiresAt = jwtService.getExpiration(token);
        long expiresInSeconds = jwtService.getExpirationMs() / 1000;

        log.info("Token generated for studentId={}", request.getStudentId());

        return TokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .expiresInSeconds(expiresInSeconds)
                .studentId(request.getStudentId())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .build();
    }

    /**
     * Validate a token and return its decoded claims.
     */
    public TokenValidationResponse validateToken(String token) {
        try {
            Claims claims = jwtService.validateAndGetClaims(token);

            return TokenValidationResponse.builder()
                    .valid(true)
                    .facultyId(claims.get("facultyId", Long.class))
                    .departmentId(claims.get("departmentId", Long.class))
                    .studentId(claims.get("studentId", String.class))
                    .email(claims.get("email", String.class))
                    .firstName(claims.get("firstName", String.class))
                    .expiresAt(claims.getExpiration().toInstant())
                    .message("Token is valid")
                    .build();

        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token is invalid or expired: " + e.getMessage())
                    .build();
        }
    }

    public String getToken(HttpServletRequest servletRequest){
        String authHeader = servletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }else {
            throw new NotFoundException("Authorization header not found.");
        }
    }
}
