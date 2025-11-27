package com.app.inventario.service;

import com.app.inventario.dto.*;
import java.util.List;

public interface ProduccionService {
    List<ProduccionResponse> listar();
    ProduccionResponse obtenerPorId(Long id);
    ProduccionResponse crear(ProduccionRequest request);
    ProduccionResponse actualizar(Long id, ProduccionRequest request);
    void eliminar(Long id);
    ProduccionResponse completarProduccion(Long id);

    // Métodos para gestión de recetas y detalles
    ProduccionResponse agregarDetallesDesdeReceta(Long produccionId, Long recetaId);
    ProduccionResponse agregarDetalleProducto(Long produccionId, DetalleProduccionRequest detalle);
    void eliminarDetalle(Long detalleId);
}
