package com.app.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockRequest(
        @NotNull Long insumoId,
        @NotNull @Min(0) Long cantidadActual,
        String ubicacion,
        Long loteProveedorId,
        Long proveedorId
) {}
