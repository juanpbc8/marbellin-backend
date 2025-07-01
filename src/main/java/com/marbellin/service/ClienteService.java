package com.marbellin.service;

import com.marbellin.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> listar();

    Cliente guardar(Cliente cliente);

    void eliminar(Long id);

    Optional<Cliente> iniciarSesion(String correo, String contrasena);

    boolean existePorCorreo(String correo);

}
