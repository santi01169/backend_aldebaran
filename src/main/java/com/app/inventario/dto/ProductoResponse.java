package com.app.inventario.dto;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer stockMinimo,
        Integer categoriaId,
        Integer unidadMedidaId,
        String categoriaNombre,
        String unidadMedidaNombre
) {}
