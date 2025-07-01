package com.marbellin.service;

import com.marbellin.entity.CategoriaProducto;
import java.util.List;

public interface CategoriaProductoService {
    List<CategoriaProducto> listar();
    CategoriaProducto guardar(CategoriaProducto categoria);
    void eliminar(Long id);
}
