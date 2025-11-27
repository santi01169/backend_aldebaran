package com.app.inventario.dto;

import jakarta.validation.constraints.*;

public record DetalleProduccionRequest(
        @NotNull Long productoId,
        @NotNull @Min(1) Integer cantidadUtilizada,
        String observacion
) {}