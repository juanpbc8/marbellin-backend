package com.marbellin.controller;

import com.marbellin.entity.VariacionProducto;
import com.marbellin.service.VariacionProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variaciones")
@Tag(name = "Variaciones de producto", description = "Operaciones con combinaciones talla + color")
public class VariacionProductoController {

    private final VariacionProductoService service;

    public VariacionProductoController(VariacionProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<VariacionProducto> listar() {
        return service.listar();
    }

    @PostMapping
    public VariacionProducto guardar(@RequestBody VariacionProducto variacion) {
        return service.guardar(variacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}