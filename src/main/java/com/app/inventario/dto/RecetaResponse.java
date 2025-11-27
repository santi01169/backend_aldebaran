package com.app.inventario.dto;

import java.util.List;

public record RecetaResponse(
        Long id,
        String nombre,
        Integer cantidadBaseProducida,
        boolean activo,
        List<DetalleRecetaResponse> ingredientes
) {}
