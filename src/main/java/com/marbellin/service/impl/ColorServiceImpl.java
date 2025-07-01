package com.marbellin.service.impl;

import com.marbellin.entity.Color;
import com.marbellin.repository.ColorRepository;
import com.marbellin.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    private final ColorRepository repo;

    public ColorServiceImpl(ColorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Color> listar() {
        return repo.findAll();
    }

    @Override
    public Color guardar(Color color) {
        return repo.save(color);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
