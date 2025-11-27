package com.app.inventario.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recetas")
public class RecetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "cantidad_base_producida", nullable = false)
    private Integer cantidadBaseProducida;

    @Column(name = "activo")
    private boolean activo;

    @Builder.Default  // ⬅️ AGREGAR ESTA ANOTACIÓN
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleRecetaEntity> ingredientes = new ArrayList<>();
}
