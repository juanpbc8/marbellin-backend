package com.marbellin.repository;

import com.marbellin.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByCorreoAndContrasena(String correo, String contrasena);
}
