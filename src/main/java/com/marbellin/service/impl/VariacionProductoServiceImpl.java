package com.marbellin.service.impl;

import com.marbellin.entity.VariacionProducto;
import com.marbellin.repository.VariacionProductoRepository;
import com.marbellin.service.VariacionProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariacionProductoServiceImpl implements VariacionProductoService {

    private final VariacionProductoRepository repo;

    public VariacionProductoServiceImpl(VariacionProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<VariacionProducto> listar() {
        return repo.findAll();
    }

    @Override
    public VariacionProducto guardar(VariacionProducto variacion) {
        return repo.save(variacion);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}