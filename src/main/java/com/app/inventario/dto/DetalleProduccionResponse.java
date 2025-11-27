package com.app.inventario.dto;

public record DetalleProduccionResponse(
        Long id,
        Long productoId,
        String productoNombre,
        Integer cantidadUtilizada,
        String observacion
) {}