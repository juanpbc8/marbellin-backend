package com.marbellin.auth.dto;

public record AuthResponse(
        Long id,
        String email,
        String rol,
        boolean authenticated,
        String token
) {
}
