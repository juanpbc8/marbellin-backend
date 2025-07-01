package com.marbellin.service;

import com.marbellin.entity.VariacionProducto;
import java.util.List;

public interface VariacionProductoService {
    List<VariacionProducto> listar();
    VariacionProducto guardar(VariacionProducto variacion);
    void eliminar(Long id);
}