package com.app.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduccionPendienteDTO {
    private Long id;
    private String orden;
    private Integer cantidadAProducir;
    private LocalDate fechaProduccion;
    private String estado;
}
