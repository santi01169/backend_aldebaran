package com.app.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoStockBajoDTO {
    private Long id;
    private String nombre;
    private Integer cantidadActual;
    private Integer stockMinimo;
    private Double porcentaje;
}
