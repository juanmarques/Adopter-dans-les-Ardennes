package com.jme.adopterdla.adopterdla.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("api/user")
@RestController
public class UserController {

    @GetMapping("/test")
    public Mono<ResponseEntity<String>> test() {
        return Mono.just(ResponseEntity.ok("Not implemented yet"));
    }
}
