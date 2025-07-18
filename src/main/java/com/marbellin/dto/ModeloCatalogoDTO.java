package com.marbellin.dto;

import java.math.BigDecimal;

public record ModeloCatalogoDTO(
        Long idModelo,
        Long idProducto,
        String nombreModelo,
        BigDecimal precio,
        String imagen,
        String categoria
) {
}