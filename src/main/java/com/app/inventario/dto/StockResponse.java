package com.app.inventario.dto;

import java.time.LocalDate;

public record StockResponse(
        Integer id,
        Long insumoId,
        String insumoNombre,
        Integer categoriaId,
        String categoriaNombre,
        Integer unidadMedidaId,
        String unidadMedidaNombre,
        Integer cantidadActual,
        String ubicacion,
        Long loteProveedorId,
        Long proveedorId,
        String proveedorNombre,
        LocalDate fechaActualizacion,
        LocalDate fechaVencimiento,
        String numeroLote,
        Integer stockMinimo
) {}
