package com.marbellin.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_variants")
public class ProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El SKU ahora vive aquí. Ej: BIKINI-FLO-S-ROJ
    @Column(nullable = false, unique = true)
    private String sku;

    // Usamos String para máxima flexibilidad (Ej: "S", "M", "34B", "One Size")
    @Column(nullable = false, length = 10)
    private String size;

    // Usamos String para permitir matices (Ej: "Rojo", "Vino", "Rojo Carmesí")
    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
