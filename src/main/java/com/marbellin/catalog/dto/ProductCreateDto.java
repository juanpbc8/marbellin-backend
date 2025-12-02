package com.marbellin.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos para crear un nuevo producto")
public record ProductCreateDto(
        @Schema(description = "Nombre del producto", example = "Laptop HP Pavilion 14", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Product name is required")
        String name,

        @Schema(description = "Descripción detallada del producto", example = "Laptop ultradelgada con pantalla de 14 pulgadas...", nullable = true)
        String description,

        @Schema(description = "Precio del producto", example = "899.99", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @Schema(description = "URL de la imagen principal del producto", example = "https://example.com/products/hp-pavilion.jpg", nullable = true)
        String imageUrl,

        @Schema(description = "Estado del producto", example = "ACTIVO", allowableValues = {"ACTIVO", "INACTIVO"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Status is required")
        String status,

        @Schema(description = "ID de la categoría a la que pertenece el producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Category is required")
        Long categoryId,

        @NotEmpty(message = "At least one variant is required")
        List<ProductVariantCreateDto> variants // <--- CAMBIO IMPORTANTE
) {
}
