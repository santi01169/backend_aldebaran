package com.app.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovimientoStockRequest(
        @NotNull @Min(1) Integer cantidad,
        String motivo
) {}
