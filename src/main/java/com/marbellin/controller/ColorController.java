package com.marbellin.controller;

import com.marbellin.entity.Color;
import com.marbellin.service.ColorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colores")
@Tag(name = "Colores", description = "Operaciones con colores de producto")
public class ColorController {

    private final ColorService service;

    public ColorController(ColorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Color> listar() {
        return service.listar();
    }

    @PostMapping
    public Color guardar(@RequestBody Color color) {
        return service.guardar(color);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
