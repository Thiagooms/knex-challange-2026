package com.knex.backend.dtos.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionCreateDto(
    @NotNull(message = "ID do produto é obrigatório")
    Long productId,

    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    Integer quantity
) {
}
