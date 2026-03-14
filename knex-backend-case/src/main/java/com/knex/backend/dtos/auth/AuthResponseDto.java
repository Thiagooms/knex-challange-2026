package com.knex.backend.dtos.auth;

import com.knex.backend.dtos.user.UserResponseDto;

public record AuthResponseDto(
    String token,
    UserResponseDto user
) {
}
