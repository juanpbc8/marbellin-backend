package com.marbellin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "talla")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTalla;

    @Column(nullable = false, unique = true, length = 20)
    private String nombreTalla;
    
    // Relación bidireccional con VariacionProducto
    @OneToMany(mappedBy = "talla", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VariacionProducto> variaciones;
}
