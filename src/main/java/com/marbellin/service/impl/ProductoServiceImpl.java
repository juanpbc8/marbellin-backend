package com.marbellin.service.impl;

import com.marbellin.entity.Producto;
import com.marbellin.repository.ProductoRepository;
import com.marbellin.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;

    public ProductoServiceImpl(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Producto> listar() {
        return repo.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        return repo.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
