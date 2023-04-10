package com.jme.adopterdla.adopterdla.auth.dto;

import java.util.List;

public record JwtResponse(String token, String refreshToken, String username, List<String> roles) {
}
