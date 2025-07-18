package com.marbellin.service.impl;

import com.marbellin.entity.Cliente;
import com.marbellin.entity.Pedido1;
import com.marbellin.entity.PedidoItem;
import com.marbellin.repository.ClienteRepository;
import com.marbellin.repository.Pedido1Repository;
import com.marbellin.service.Pedido1Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class Pedido1ServiceImpl implements Pedido1Service {

    private final Pedido1Repository pedidoRepo;
    private final ClienteRepository clienteRepo;

    public Pedido1ServiceImpl(Pedido1Repository pedidoRepo,
                              ClienteRepository clienteRepo) {
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    @Transactional
    public Pedido1 crearPedido(Pedido1 pedido) {
        // 1. Reemplazar transient Cliente por proxy gestionado
        Long idCliente = pedido.getCliente().getIdCliente();
        Cliente cliente = clienteRepo.getReferenceById(idCliente);
        pedido.setCliente(cliente);

        // 2. Calcular subtotales y total
        pedido.getItems().forEach(item -> {
            item.setPedido(pedido);
            item.setSubtotal(
                    item.getPrecioUnitario()
                            .multiply(new BigDecimal(item.getCantidad()))
            );
        });
        BigDecimal total = pedido.getItems().stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);

        // 3. Persistir pedido + items
        return pedidoRepo.save(pedido);
    }
}