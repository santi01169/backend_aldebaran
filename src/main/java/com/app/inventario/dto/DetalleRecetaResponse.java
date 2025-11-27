package com.app.inventario.dto;

public record DetalleRecetaResponse(
        Long id,
        Long insumoId,
        String nombreInsumo,
        Integer cantidadRequerida
) {}
