package com.marbellin.controller;

import com.marbellin.entity.Pedido1;
import com.marbellin.service.Pedido1Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos1")
public class Pedido1Controller {

    private final Pedido1Service service;

    public Pedido1Controller(Pedido1Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pedido1> crearPedido(@RequestBody Pedido1 pedido) {
        Pedido1 guardado = service.crearPedido(pedido);
        return ResponseEntity.status(201).body(guardado);
    }
}
