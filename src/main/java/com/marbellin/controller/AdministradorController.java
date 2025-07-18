package com.marbellin.controller;

import com.marbellin.dto.AdminDTO;
import com.marbellin.dto.LoginRequestDTO;
import com.marbellin.entity.Administrador;
import com.marbellin.service.AdministradorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        Administrador admin = service.buscarPorCorreoYContrasena(dto.correo(), dto.contrasena());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        AdminDTO dtoResponse = new AdminDTO(admin.getIdAdministrador(), admin.getCorreo(), admin.getFechaRegistro().toString());
        return ResponseEntity.ok(dtoResponse);
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarPorAdmin(@RequestBody Administrador nuevoAdmin,
                                             @RequestParam("idAdminSolicitante") Long idSolicitante) {
        Administrador adminSolicitante = service.buscarPorId(idSolicitante);

        if (adminSolicitante == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para registrar nuevos administradores");
        }

        Administrador guardado = service.guardar(nuevoAdmin);
        AdminDTO dto = new AdminDTO(guardado.getIdAdministrador(), guardado.getCorreo(), guardado.getFechaRegistro().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

}
