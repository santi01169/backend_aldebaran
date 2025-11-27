package com.app.inventario.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

// ProduccionRequest.java - Ajustado
public record ProduccionRequest(
        @NotBlank String orden,
        @NotNull Integer cantidadAProducir,
        LocalDate fechaProduccion,
        String estado,
        Integer usuarioResponsableId,
        String observacion,
        List<DetalleProduccionRequest> detalles // PUEDE SER VACÍO o NULL
) {}
