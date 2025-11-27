package com.app.inventario.service;

import com.app.inventario.dto.*;
import java.util.List;

public interface RecetaService {
    RecetaResponse crear(RecetaRequest request);
    List<RecetaResponse> listar();
    RecetaResponse obtenerPorId(Long id);
    RecetaResponse actualizar(Long id, RecetaRequest request);
    void eliminar(Long id);

    // Métodos nuevos para gestión de ingredientes paso a paso
    RecetaResponse agregarIngrediente(Long recetaId, DetalleRecetaRequest detalle);
    void eliminarIngrediente(Long ingredienteId);
    List<DetalleProduccionRequest> obtenerDetallesDeReceta(Long recetaId);
}
