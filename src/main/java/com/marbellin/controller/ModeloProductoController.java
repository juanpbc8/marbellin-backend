package com.marbellin.controller;

import com.marbellin.entity.ModeloProducto;
import com.marbellin.service.ModeloProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
@Tag(name = "Modelos de producto", description = "CRUD para modelos (ej: blonda, encaje...)")
public class ModeloProductoController {

    private final ModeloProductoService service;

    public ModeloProductoController(ModeloProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ModeloProducto> listar() {
        return service.listar();
    }

    @PostMapping
    public ModeloProducto guardar(@RequestBody ModeloProducto modelo) {
        return service.guardar(modelo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
