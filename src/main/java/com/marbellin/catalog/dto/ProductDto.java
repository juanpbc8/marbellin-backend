package com.marbellin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Producto del catálogo")
public record ProductDto(
        @Schema(description = "ID único del producto", example = "1")
        Long id,

        @Schema(description = "Nombre del producto", example = "Laptop Dell Inspiron 15")
        String name,

        @Schema(description = "Descripción detallada del producto", example = "Laptop con procesador Intel Core i5...")
        String description,

        @Schema(description = "Precio del producto", example = "799.99")
        BigDecimal price,

        @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/laptop.jpg")
        String imageUrl,

        @Schema(description = "Estado del producto", example = "ACTIVO", allowableValues = {"ACTIVO", "INACTIVO"})
        String status,

        @Schema(description = "Lista de variantes disponibles")
        List<ProductVariantDto> variants, // <--- CAMBIO IMPORTANTE

        @Schema(description = "Categoría del producto")
        CategoryDto category,

        @Schema(description = "Fecha de creación del producto", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Fecha de última actualización del producto", example = "2024-01-15T10:30:00")
        LocalDateTime updatedAt
) {
}
