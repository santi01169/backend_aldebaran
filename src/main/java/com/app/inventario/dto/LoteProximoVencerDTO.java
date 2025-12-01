package com.app.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteProximoVencerDTO {
    private Long id;
    private String numeroLote;
    private String nombreProducto;
    private LocalDate fechaVencimiento;
    private Long diasRestantes;
}
