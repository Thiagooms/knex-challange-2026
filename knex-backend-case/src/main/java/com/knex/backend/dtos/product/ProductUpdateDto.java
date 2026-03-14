package com.knex.backend.dtos.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductUpdateDto(
    @Size(min = 2, max = 255, message = "Nome deve ter entre 2 e 255 caracteres")
    String name,

    @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
    String description,

    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    BigDecimal price
) {
}
