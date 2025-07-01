package com.marbellin.controller;

import com.marbellin.entity.Talla;
import com.marbellin.service.TallaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tallas")
@Tag(name = "Tallas", description = "Operaciones con tallas de producto")
public class TallaController {

    private final TallaService service;

    public TallaController(TallaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Talla> listar() {
        return service.listar();
    }

    @PostMapping
    public Talla guardar(@RequestBody Talla talla) {
        return service.guardar(talla);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
