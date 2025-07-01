package com.marbellin.controller;

import com.marbellin.entity.Administrador;
import com.marbellin.service.AdministradorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@Tag(name = "Administradores", description = "Gestión de administradores del sistema")
public class AdministradorController {

    private final AdministradorService service;

    public AdministradorController(AdministradorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Administrador> listar() {
        return service.listar();
    }

    @PostMapping
    public Administrador guardar(@RequestBody Administrador admin) {
        return service.guardar(admin);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
