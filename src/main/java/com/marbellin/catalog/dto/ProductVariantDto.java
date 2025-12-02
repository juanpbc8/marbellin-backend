package com.marbellin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Variante de producto (Talla/Color)")
public record ProductVariantDto(
        Long id,
        String sku,
        String size,
        String color,
        Integer stock
) {
}
