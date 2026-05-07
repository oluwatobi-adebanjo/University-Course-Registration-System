package com.unilag.course_registration_system.session;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.List;

@Configuration
@Getter
@Setter
public class CorsConfig implements WebMvcConfigurer{
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${cors.max-age}")
    private long maxAge;

    /**
     * Global CORS mapping applied to all endpoints via WebMvcConfigurer.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        String[] methods = allowedMethods.split(",");

        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods(methods)
                .allowedHeaders(allowedHeaders.equals("*") ? new String[]{"*"} : allowedHeaders.split(","))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    /**
     * CorsConfigurationSource bean — used by Spring Security (if added later)
     * and available as a standalone CorsFilter.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        List<String> methods = Arrays.asList(allowedMethods.split(","));

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(methods);

        if (allowedHeaders.equals("*")) {
            config.addAllowedHeader("*");
        } else {
            config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        }

        config.setAllowCredentials(allowCredentials);
        config.setMaxAge(maxAge);

        // Expose Authorization header to the client
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    /**
     * Register the CorsFilter so OPTIONS pre-flight requests are handled
     * before any other filter/security chain.
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
