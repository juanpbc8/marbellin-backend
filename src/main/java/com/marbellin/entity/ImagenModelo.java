package com.marbellin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagen_modelo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImagenModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagen;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer orden;

    // FKs
    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private ModeloProducto modelo;
}
