package com.marbellin.service.impl;

import com.marbellin.entity.ImagenModelo;
import com.marbellin.repository.ImagenModeloRepository;
import com.marbellin.service.ImagenModeloService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagenModeloServiceImpl implements ImagenModeloService {

    private final ImagenModeloRepository repo;

    public ImagenModeloServiceImpl(ImagenModeloRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ImagenModelo> listar() {
        return repo.findAll();
    }

    @Override
    public ImagenModelo guardar(ImagenModelo imagen) {
        return repo.save(imagen);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
