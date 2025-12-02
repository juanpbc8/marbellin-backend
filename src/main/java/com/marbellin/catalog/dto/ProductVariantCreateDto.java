package com.marbellin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para crear una variante")
public record ProductVariantCreateDto(
        @NotBlank(message = "SKU is required")
        String sku,

        @NotBlank(message = "Size is required")
        String size,

        @NotBlank(message = "Color is required")
        String color,

        @NotNull(message = "Stock is required")
        @Min(0)
        Integer stock
) {
}
