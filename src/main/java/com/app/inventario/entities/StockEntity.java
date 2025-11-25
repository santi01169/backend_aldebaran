package com.app.inventario.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"Stock\"") // La tabla en la BD se llama "Stock" (con mayúscula y comillas)
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idStock\"")
    private Integer id;

    @Column(name = "\"Cantidad_actual\"", nullable = false)
    private Integer cantidadActual;

    @Column(name = "\"Fecha_actualizacion\"")
    private LocalDate fechaActualizacion;

    @Column(name = "\"Ubicacion\"", length = 45)
    private String ubicacion;

    // =============== RELACIONES =================

    // FK: "Insumos_idInsumos" → tabla "Insumos"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Insumos_idInsumos\"", nullable = false)
    private ProductoEntity insumo;

    // FK: "Insumos_Categoria_idCategoria" → tabla "Categoria"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Insumos_Categoria_idCategoria\"")
    private CategoriaEntity categoria;

    // FK: "Insumos_Unidad_medida_idUnidad_medida" → tabla "Unidad_medida"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Insumos_Unidad_medida_idUnidad_medida\"")
    private UnidadMedidaEntity unidadMedida;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_proveedor_id")
    private LoteProveedorEntity loteProveedor;


}
