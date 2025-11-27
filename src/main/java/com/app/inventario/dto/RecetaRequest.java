package com.app.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RecetaRequest(
        @NotNull String nombre,
        @Min(1) Integer cantidadBaseProducida,
        boolean activo,
        List<DetalleRecetaRequest> ingredientes // ⬅️ Ahora es OPCIONAL (puede ser null o vacío)
) {}
