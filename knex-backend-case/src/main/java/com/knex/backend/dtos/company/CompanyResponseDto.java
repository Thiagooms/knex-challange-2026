package com.knex.backend.dtos.company;

import java.time.LocalDateTime;

public record CompanyResponseDto(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
