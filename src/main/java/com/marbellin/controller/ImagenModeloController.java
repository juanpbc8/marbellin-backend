package com.marbellin.controller;

import com.marbellin.entity.ImagenModelo;
import com.marbellin.service.ImagenModeloService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@Tag(name = "Imágenes de modelos", description = "Gestión de imágenes para cada modelo de producto")
public class ImagenModeloController {

    private final ImagenModeloService service;

    public ImagenModeloController(ImagenModeloService service) {
        this.service = service;
    }

    @GetMapping
    public List<ImagenModelo> listar() {
        return service.listar();
    }

    @PostMapping
    public ImagenModelo guardar(@RequestBody ImagenModelo imagen) {
        return service.guardar(imagen);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
