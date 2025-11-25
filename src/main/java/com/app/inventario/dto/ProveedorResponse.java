package com.app.inventario.dto;

public record ProveedorResponse(
        Long id,
        String nombre,
        String contacto,
        String telefono,
        String email,
        String direccion
) {}
