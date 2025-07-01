package com.marbellin.service;

import com.marbellin.entity.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listar();
    Producto guardar(Producto producto);
    void eliminar(Long id);
}
