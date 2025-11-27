package com.app.inventario.dto;

import java.time.LocalDate;
import java.util.List;

public record ProduccionResponse(
        Long id,
        String orden,
        Integer cantidadAProducir,
        LocalDate fechaProduccion,
        String estado,
        Integer usuarioResponsableId,
        String usuarioResponsableNombre,
        String observacion,
        List<DetalleProduccionResponse> detalles
) {}