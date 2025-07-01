package com.marbellin.repository;

import com.marbellin.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreoAndContrasena(String correo, String contrasenia);

    boolean existsByCorreo(String correo);

}
