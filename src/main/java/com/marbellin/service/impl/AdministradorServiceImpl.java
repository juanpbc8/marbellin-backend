package com.marbellin.service.impl;

import com.marbellin.entity.Administrador;
import com.marbellin.repository.AdministradorRepository;
import com.marbellin.service.AdministradorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository repo;

    public AdministradorServiceImpl(AdministradorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Administrador> listar() {
        return repo.findAll();
    }

    @Override
    public Administrador guardar(Administrador admin) {
        return repo.save(admin);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
