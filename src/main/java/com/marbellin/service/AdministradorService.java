package com.marbellin.service;

import com.marbellin.entity.Administrador;

import java.util.List;

public interface AdministradorService {
    List<Administrador> listar();

    Administrador guardar(Administrador admin);

    void eliminar(Long id);

    Administrador buscarPorId(Long id);

    Administrador buscarPorCorreoYContrasena(String correo, String contrasena);
}
