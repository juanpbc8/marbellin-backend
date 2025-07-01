package com.marbellin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "variacion_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVariacion;

    @Column(nullable = false)
    private Integer stock;

    // FKs
    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private ModeloProducto modelo;

    @ManyToOne
    @JoinColumn(name = "id_color", nullable = false)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "id_talla", nullable = false)
    private Talla talla;

    // Relación bidireccional con DetallePedido
    @OneToMany(mappedBy = "variacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DetallePedido> detalles;
}
