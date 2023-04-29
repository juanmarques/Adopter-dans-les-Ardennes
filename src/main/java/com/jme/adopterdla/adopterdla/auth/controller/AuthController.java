package com.jme.adopterdla.adopterdla.auth.controller;

import com.jme.adopterdla.adopterdla.auth.CustomUserDetails;
import com.jme.adopterdla.adopterdla.auth.dto.JwtResponse;
import com.jme.adopterdla.adopterdla.auth.dto.LoginRequest;
import com.jme.adopterdla.adopterdla.configs.security.JwtAuthenticationManager;
import com.jme.adopterdla.adopterdla.configs.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * The AuthController class handles authentication related HTTP requests.
 */
@RestController
@RequestMapping("api/auth")
@Log4j2
public class AuthController {

    private final JwtAuthenticationManager authenticationManager;

    private final ReactiveUserDetailsService userDetailService;

    private final JwtService jwtService;

    /**
     * Constructs a new AuthController with the given JwtAuthenticationManager, JwtService,
     * and ReactiveUserDetailsService.
     *
     * @param authenticationManager The JwtAuthenticationManager used to authenticate users.
     * @param userDetailService     The ReactiveUserDetailsService used to retrieve user details.
     * @param jwtService            The JwtService used to generate and validate JWTs.
     */
    public AuthController(JwtAuthenticationManager authenticationManager, ReactiveUserDetailsService userDetailService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user using the provided loginRequest.
     *
     * @param loginRequest The LoginRequest containing the username and password to authenticate.
     * @return A Mono wrapping a ResponseEntity containing a JwtResponse with the JWT and user details,
     * or a ResponseEntity with status UNAUTHORIZED if authentication fails.
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticates a user and generates a JWT token for further requests.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful."),
            @ApiResponse(responseCode = "401", description = "Authentication failed, invalid credentials.")
    })
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()))
                .flatMap(authentication -> userDetailService.findByUsername(authentication.getName())
                        // Find the user details using the user details service based on the authenticated username
                        .map(userDetails -> {
                            var token = jwtService.generateToken(authentication, false);
                            var refreshToken = jwtService.generateToken(authentication, true);
                            return ResponseEntity.ok(new JwtResponse(token, refreshToken, ((CustomUserDetails) userDetails).getFriendlyName(),
                                    userDetails.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .toList()));
                        }))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Invalid username or password.")))
                // Throw an error if the user is not found
                .onErrorResume(AuthenticationException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    /**
     * Generates a new JWT using the provided refresh token.
     *
     * @param refreshToken The refresh token to use for generating a new JWT.
     * @return A Mono wrapping a ResponseEntity containing a JwtResponse with the new JWT and user details,
     * or a ResponseEntity with status UNAUTHORIZED if the refresh token is invalid.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Generates a new JWT using the provided refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT refreshed successfully."),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token.")
    })
    public Mono<ResponseEntity<JwtResponse>> refresh(@RequestParam("refreshToken") String refreshToken) {

        try {
            jwtService.validateToken(refreshToken);
            String username = jwtService.extractUsername(refreshToken);
            return userDetailService.findByUsername(username)
                    .map(userDetails -> (CustomUserDetails) userDetails)
                    // Cast the UserDetails object to CustomUserDetails to retrieve the friendlyName field
                    .flatMap(customUserDetails -> {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String newAccessToken = jwtService.generateToken(authentication, false);
                        String newRefreshToken = jwtService.generateToken(authentication, true);
                        return Mono.just(ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken, customUserDetails.getFriendlyName(), customUserDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())));
                    });
        } catch (
                ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException |
                SignatureException |
                IllegalArgumentException jwtException) {
            log.error("Invalid refresh token: {}", jwtException.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }


}
