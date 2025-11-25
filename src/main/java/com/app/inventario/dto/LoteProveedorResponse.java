package com.app.inventario.dto;

import java.time.LocalDate;

public record LoteProveedorResponse(
        Long id,
        String numeroLote,
        LocalDate fechaIngreso,
        LocalDate fechaVencimiento,
        Integer cantidadInicial,
        Long proveedorId,
        String proveedorNombre,
        Long productoId,
        String productoNombre
) {}
