package com.marbellin.dto;

import java.math.BigDecimal;
import java.util.List;

public record ModeloProductoDTO(
        String nombreModelo,
        String descripcion,
        BigDecimal precio,
        List<ImagenModeloDTO> imagenes,
        List<VariacionProductoDTO> variaciones
) {
}