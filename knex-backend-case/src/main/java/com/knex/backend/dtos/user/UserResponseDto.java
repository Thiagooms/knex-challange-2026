package com.knex.backend.dtos.user;

import com.knex.backend.dtos.company.CompanyResponseDto;

import java.time.LocalDateTime;

public record UserResponseDto(
    Long id,
    String email,
    String role,
    CompanyResponseDto company,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
