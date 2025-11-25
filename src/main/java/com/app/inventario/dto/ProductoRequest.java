package com.app.inventario.dto;

import jakarta.validation.constraints.*;

public record ProductoRequest(
        @NotBlank String nombre,
        String descripcion,
        @Min(0) Integer stockMinimo,
        Integer categoriaId,
        Integer unidadMedidaId
) {}
