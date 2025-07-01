package com.marbellin.service;

import com.marbellin.entity.DetallePedido;
import java.util.List;

public interface DetallePedidoService {
    List<DetallePedido> listar();
    DetallePedido guardar(DetallePedido detallePedido);
    void eliminar(Long id);
}
