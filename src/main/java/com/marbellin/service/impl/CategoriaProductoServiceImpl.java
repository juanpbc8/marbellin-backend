package com.marbellin.service.impl;

import com.marbellin.entity.CategoriaProducto;
import com.marbellin.repository.CategoriaProductoRepository;
import com.marbellin.service.CategoriaProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    private final CategoriaProductoRepository repo;

    public CategoriaProductoServiceImpl(CategoriaProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CategoriaProducto> listar() {
        return repo.findAll();
    }

    @Override
    public CategoriaProducto guardar(CategoriaProducto categoria) {
        return repo.save(categoria);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
