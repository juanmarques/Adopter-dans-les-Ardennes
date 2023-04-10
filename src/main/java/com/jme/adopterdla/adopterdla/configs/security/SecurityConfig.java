package com.jme.adopterdla.adopterdla.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

/**
 * Configuration class for Spring Security in a reactive web application.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructs a new instance of {@link SecurityConfig}.
     * @param jwtAuthenticationFilter the {@link JwtAuthenticationFilter} to be used for JWT authentication
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the Spring Security filter chain.
     * @param http the {@link ServerHttpSecurity} object to be configured
     * @param authenticationManager the {@link ReactiveAuthenticationManager} object to be used for authentication
     * @return the configured {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveAuthenticationManager authenticationManager) {
        // Disable CSRF protection
        http.csrf().disable()
                // Authorize access to /api/auth/** for all users
                .authorizeExchange()
                .pathMatchers("/api/auth/**").permitAll()
                // Require authentication for all other requests
                .anyExchange().authenticated()
                .and()
                // Add the JWT authentication filter to the filter chain
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // Set the authentication manager
                .authenticationManager(authenticationManager);
        return http.build();
    }

    /**
     * Returns a {@link ServerWebExchangeMatcher} for matching request paths.
     * @return a {@link PathPatternParserServerWebExchangeMatcher} that matches paths starting with /api/**
     */
    @Bean
    public ServerWebExchangeMatcher pathMatcher() {
        return new PathPatternParserServerWebExchangeMatcher("/api/**");
    }
}