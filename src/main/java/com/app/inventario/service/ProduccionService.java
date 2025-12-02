package com.app.inventario.service;

import com.app.inventario.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProduccionService {
    List<ProduccionResponse> listar();
    ProduccionResponse obtenerPorId(Long id);
    ProduccionResponse crear(ProduccionRequest request);
    ProduccionResponse actualizar(Long id, ProduccionRequest request);
    void eliminar(Long id);
    ProduccionResponse completarProduccion(Long id);

    ProduccionResponse agregarDetallesDesdeReceta(Long produccionId, Long recetaId);
    ProduccionResponse agregarDetalleProducto(Long produccionId, DetalleProduccionRequest detalle);
    void eliminarDetalle(Long detalleId);

    Page<ProduccionResponse> filtrar(String orden,
                                     String estado,
                                     LocalDate fechaDesde,
                                     LocalDate fechaHasta,
                                     Long usuarioResponsableId,
                                     Pageable pageable);
}
