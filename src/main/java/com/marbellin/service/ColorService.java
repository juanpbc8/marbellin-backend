package com.marbellin.service;

import com.marbellin.entity.Color;
import java.util.List;

public interface ColorService {
    List<Color> listar();
    Color guardar(Color color);
    void eliminar(Long id);
}
