package com.marbellin.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Item de producto en la orden")
public record OrderItemDto(
        @Schema(description = "ID del item", example = "1")
        Long id,

        @Schema(description = "ID del producto", example = "10")
        Long productId,

        @Schema(description = "Nombre del producto", example = "Bikini Azul Verano")
        String productName,

        @Schema(description = "SKU de la variante especifica", example = "BIK-AZU-055")
        String variantSku,

        @Schema(description = "Talla seleccionada", example = "S")
        String selectedSize,

        @Schema(description = "Color seleccionado", example = "Rojo")
        String selectedColor,

        @Schema(description = "URL de imagen del producto")
        String productImageUrl,

        @Schema(description = "Cantidad", example = "2")
        Integer quantity,

        @Schema(description = "Precio unitario", example = "250.00")
        BigDecimal unitPrice,
        
        @Schema(description = "Subtotal (cantidad * precio)", example = "100.00")
        BigDecimal subtotal // <--- Agregado para el Frontend
) {
}

