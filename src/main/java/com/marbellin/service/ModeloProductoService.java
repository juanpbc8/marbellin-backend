package com.marbellin.service;

import com.marbellin.entity.ModeloProducto;
import java.util.List;

public interface ModeloProductoService {
    List<ModeloProducto> listar();
    ModeloProducto guardar(ModeloProducto modelo);
    void eliminar(Long id);
}
