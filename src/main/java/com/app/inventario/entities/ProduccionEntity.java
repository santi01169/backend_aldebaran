package com.app.inventario.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produccion")
public class ProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden", nullable = false, length = 100)
    private String orden;

    @Column(name = "cantidad_a_producir", nullable = false)
    private Integer cantidadAProducir;

    @Column(name = "fecha_produccion")
    private LocalDate fechaProduccion;

    @Column(name = "estado", length = 50)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsable_id")
    private UsuarioEntity usuarioResponsable;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Builder.Default  // ⬅️ AGREGAR ESTA ANOTACIÓN
    @OneToMany(mappedBy = "produccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleProduccionEntity> detalles = new ArrayList<>();
}
