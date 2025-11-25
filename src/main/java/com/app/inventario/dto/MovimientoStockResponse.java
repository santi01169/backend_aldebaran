package com.app.inventario.dto;

import java.time.LocalDateTime;

public record MovimientoStockResponse(
        Long id,
        String tipo,          // ENTRADA / SALIDA
        Integer cantidad,
        Integer stockAntes,
        Integer stockDespues,
        String motivo,
        LocalDateTime fechaMovimiento
) {}
