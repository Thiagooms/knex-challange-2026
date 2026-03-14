package com.knex.backend.dtos.transaction;

import com.knex.backend.dtos.product.ProductResponseDto;
import com.knex.backend.dtos.user.UserResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
    Long id,
    Integer quantity,
    BigDecimal totalPrice,
    UserResponseDto user,
    ProductResponseDto product,
    LocalDateTime createdAt
) {
}
