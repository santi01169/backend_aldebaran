package com.app.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private List<ProduccionPendienteDTO> produccionesPendientes;
    private List<ProductoStockBajoDTO> productosStockBajo;
    private List<LoteProximoVencerDTO> lotesProximosVencer;
    private List<MovimientoStockResponse> ultimosMovimientos;
}
