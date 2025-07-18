package com.marbellin.dto;

import java.math.BigDecimal;

public record ProductoCatalogoDTO(
        Long idProducto,
        String nombre,
        BigDecimal precio,
        String imagen,
        String categoria
) {
}