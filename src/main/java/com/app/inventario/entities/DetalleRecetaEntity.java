package com.app.inventario.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "detalles_receta")
public class DetalleRecetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private RecetaEntity receta;

    // El insumo necesario (Ej: Harina, Tomate)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insumo_id", nullable = false)
    private ProductoEntity insumo;

    // Cuánto se necesita para fabricar la 'cantidadBaseProducida'
    @Column(name = "cantidad_requerida", nullable = false)
    private Integer cantidadRequerida;
}
