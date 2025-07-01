package com.marbellin.service.impl;

import com.marbellin.entity.Cliente;
import com.marbellin.repository.ClienteRepository;
import com.marbellin.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return repo.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Cliente> iniciarSesion(String correo, String contrasena) {
        return repo.findByCorreoAndContrasena(correo, contrasena);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return repo.existsByCorreo(correo);
    }
}