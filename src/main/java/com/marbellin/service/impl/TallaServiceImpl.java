package com.marbellin.service.impl;

import com.marbellin.entity.Talla;
import com.marbellin.repository.TallaRepository;
import com.marbellin.service.TallaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TallaServiceImpl implements TallaService {

    private final TallaRepository repo;

    public TallaServiceImpl(TallaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Talla> listar() {
        return repo.findAll();
    }

    @Override
    public Talla guardar(Talla talla) {
        return repo.save(talla);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
