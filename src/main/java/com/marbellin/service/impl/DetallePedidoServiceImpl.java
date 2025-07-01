package com.marbellin.service.impl;

import com.marbellin.entity.DetallePedido;
import com.marbellin.repository.DetallePedidoRepository;
import com.marbellin.service.DetallePedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {
    private final DetallePedidoRepository repo;

    public DetallePedidoServiceImpl(DetallePedidoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<DetallePedido> listar() {
        return repo.findAll();
    }

    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return repo.save(detallePedido);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
