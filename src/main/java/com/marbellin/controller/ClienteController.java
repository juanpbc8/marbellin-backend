package com.marbellin.controller;

import com.marbellin.entity.Cliente;
import com.marbellin.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        if (service.existePorCorreo(cliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        Cliente guardado = service.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginData) {
        String correo = loginData.get("correo");
        String contrasena = loginData.get("contrasena");

        Optional<Cliente> clienteOpt = service.iniciarSesion(correo, contrasena);

        if (clienteOpt.isPresent()) {
            return ResponseEntity.ok(clienteOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @GetMapping("/existe")
    public ResponseEntity<?> verificarExistenciaCorreo(@RequestParam String correo) {
        boolean existe = service.existePorCorreo(correo);
        if (existe) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        } else {
            return ResponseEntity.ok("Correo disponible");
        }
    }

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
