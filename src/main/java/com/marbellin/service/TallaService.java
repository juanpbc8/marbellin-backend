package com.marbellin.service;

import com.marbellin.entity.Talla;
import java.util.List;

public interface TallaService {
    List<Talla> listar();
    Talla guardar(Talla talla);
    void eliminar(Long id);
}
