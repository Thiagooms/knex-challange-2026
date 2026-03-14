package com.knex.backend.dtos.product;

import com.knex.backend.dtos.company.CompanyResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    Long version,
    CompanyResponseDto company,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
