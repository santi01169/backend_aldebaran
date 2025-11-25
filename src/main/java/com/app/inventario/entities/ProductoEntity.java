package com.app.inventario.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "\"Insumos\"")
@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idInsumos\"")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "\"Nombre\"")
    private String nombre;

    @Size(max = 500)
    @Column(name = "\"Descripcion\"")
    private String descripcion;

    @Min(0)
    @Column(name = "\"Stock_minimo\"")
    private Integer stockMinimo;

    // ❌ ELIMINA ESTOS CAMPOS:
    // private Integer categoriaId;
    // private Integer unidadMedidaId;

    // ✅ SOLO DEJA LAS RELACIONES (sin insertable/updatable false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "\"Categoria_idCategoria\"",
            referencedColumnName = "\"idCategoria\""
    )
    private CategoriaEntity categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "\"Unidad_medida_idUnidad_medida\"",
            referencedColumnName = "\"idUnidad_medida\""
    )
    private UnidadMedidaEntity unidadMedida;
}