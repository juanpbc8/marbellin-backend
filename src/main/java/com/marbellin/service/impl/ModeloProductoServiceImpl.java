package com.marbellin.service.impl;

import com.marbellin.entity.ModeloProducto;
import com.marbellin.repository.ModeloProductoRepository;
import com.marbellin.service.ModeloProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloProductoServiceImpl implements ModeloProductoService {

    private final ModeloProductoRepository repo;

    public ModeloProductoServiceImpl(ModeloProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ModeloProducto> listar() {
        return repo.findAll();
    }

    @Override
    public ModeloProducto guardar(ModeloProducto modelo) {
        return repo.save(modelo);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
