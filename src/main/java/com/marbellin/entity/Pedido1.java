package com.marbellin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido1")
@Getter
@Setter
public class Pedido1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 8)
    private String dni;

    @Column(nullable = false, length = 15)
    private String telefono;

    // Documento
    @Column(nullable = false, length = 10)
    private String tipoDocumento; // BOLETA o FACTURA

    @Column(length = 11)
    private String ruc;

    @Column(length = 200)
    private String razonSocial;

    // Entrega
    @Column(nullable = false, length = 10)
    private String entregaTipo; // DELIVERY o TIENDA

    @Column(length = 100)
    private String departamento;

    @Column(length = 100)
    private String provincia;

    @Column(length = 100)
    private String distrito;

    @Column(length = 200)
    private String direccion;

    @Column(length = 200)
    private String referencia;

    // Pago
    @Column(nullable = false, length = 20)
    private String pagoMetodo; // TARJETA, YAPE, CONTRA_ENTREGA

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    // Relación con items
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items;
    
    // Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Este método se ejecuta justo antes de insertarse en la BD
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
