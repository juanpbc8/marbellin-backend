package com.marbellin.service;

import com.marbellin.entity.Pedido;
import java.util.List;

public interface PedidoService {
    List<Pedido> listar();
    Pedido guardar(Pedido pedido);
    void eliminar(Long id);
}
