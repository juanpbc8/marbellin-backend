package com.marbellin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marbellin.enums.EstadoPedido;
import com.marbellin.enums.MetodoPagoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MetodoPagoPedido metodoPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDate fechaPedido;

    // FKs
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Relación bidireccional con DetallePedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DetallePedido> detalles;

    @PrePersist
    public void prePersist() {
        if (fechaPedido == null) this.fechaPedido = LocalDate.now();
    }
}
