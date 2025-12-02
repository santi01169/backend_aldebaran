package com.app.inventario.service;

import com.app.inventario.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecetaService {
    RecetaResponse crear(RecetaRequest request);
    List<RecetaResponse> listar();
    RecetaResponse obtenerPorId(Long id);
    RecetaResponse actualizar(Long id, RecetaRequest request);
    void eliminar(Long id);

    RecetaResponse agregarIngrediente(Long recetaId, DetalleRecetaRequest detalle);
    void eliminarIngrediente(Long ingredienteId);
    List<DetalleProduccionRequest> obtenerDetallesDeReceta(Long recetaId);

    Page<RecetaResponse> filtrar(String nombre,
                                 Boolean activa,
                                 Integer cantidadBaseMinima,
                                 Pageable pageable);
}
