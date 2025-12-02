package com.marbellin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Datos para actualizar o crear una variante dentro de la edición de producto")
public record ProductVariantUpdateDto(
        @Schema(description = "ID de la variante. Si es nulo, se creará una nueva.")
        Long id,

        String sku, // Opcional por si quieres corregir el SKU
        String size,
        String color,

        @Min(0)
        Integer stock
) {
}
