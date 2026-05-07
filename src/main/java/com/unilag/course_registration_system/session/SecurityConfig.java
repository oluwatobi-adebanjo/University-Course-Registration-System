package com.unilag.course_registration_system.session;



import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

/**
 * Spring Security filter chain for the Student Auth API.
 *
 * Rules
 * ─────
 *  PUBLIC  – token generation, validate endpoint, health check, OPTIONS pre-flight
 *  SECURED – everything else under /api/** requires a valid Bearer JWT
 *
 * Session management is STATELESS — no HttpSession is ever created.
 * CSRF is disabled because the API is token-based, not cookie-based.
 * CORS is delegated to the CorsFilter bean defined in CorsConfig.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity           // enables @PreAuthorize / @PostAuthorize on controllers
@RequiredArgsConstructor
public class SecurityConfig {

    // ── Dependencies ──────────────────────────────────────────────────────────
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * No-op UserDetailsService — satisfies Spring Security's internal wiring
     * so it skips the inMemoryUserDetailsManager auto-configuration.
     *
     * This API never performs username/password authentication.
     * All auth is done via JWT claims in JwtAuthenticationFilter.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new NotFoundException(
                    "Use a JWT Bearer token.");
        };
    }

    // ── Filter chain ──────────────────────────────────────────────────────────

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/welcome/here").permitAll()
                        .anyRequest().permitAll()
                )

                // ── 5. Custom JWT filter — runs before username/password filter ─
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // ── 6. Custom 401 handler (returns JSON, not HTML) ──────────────
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            Response<Void> body = new Response<>(401, "Unauthorized");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            log.error("Unauthorized inside exception handling", authException);
                            objectMapper.writeValue(response.getWriter(), body);
                        })

                        // 403 handler (authenticated but lacks required role)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            Response<Void> body = new Response<>(403, "Forbidden");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getWriter(), body);
                        })
                );

        return http.build();
    }
}
