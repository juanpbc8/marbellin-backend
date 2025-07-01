package com.marbellin.service;

import com.marbellin.entity.ImagenModelo;
import java.util.List;

public interface ImagenModeloService {
    List<ImagenModelo> listar();
    ImagenModelo guardar(ImagenModelo imagen);
    void eliminar(Long id);
}
