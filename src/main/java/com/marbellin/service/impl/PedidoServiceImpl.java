package com.marbellin.service.impl;

import com.marbellin.entity.Pedido;
import com.marbellin.repository.PedidoRepository;
import com.marbellin.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository repo;

    public PedidoServiceImpl(PedidoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Pedido> listar() {
        return repo.findAll();
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return repo.save(pedido);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
