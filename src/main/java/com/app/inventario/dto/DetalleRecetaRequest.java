package com.app.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleRecetaRequest(
        @NotNull Long insumoId,
        @Min(1) Integer cantidadRequerida
) {}
