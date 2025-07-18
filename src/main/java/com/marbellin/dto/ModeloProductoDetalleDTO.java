package com.marbellin.dto;

import java.math.BigDecimal;
import java.util.List;

public record ModeloProductoDetalleDTO(
        Long idModelo,
        String nombreModelo,
        String descripcion,
        BigDecimal precio,
        String imagen,
        String categoria,
        List<VariacionDTO> variaciones
) {
}