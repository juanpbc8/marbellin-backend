package com.marbellin.controller;

import com.marbellin.entity.CategoriaProducto;
import com.marbellin.service.CategoriaProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "Operaciones relacionadas con categorías de producto")
public class CategoriaProductoController {

    private final CategoriaProductoService service;

    public CategoriaProductoController(CategoriaProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaProducto> listar() {
        return service.listar();
    }

    @PostMapping
    public CategoriaProducto guardar(@RequestBody CategoriaProducto categoria) {
        return service.guardar(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
