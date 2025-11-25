package com.app.inventario.service;

import com.app.inventario.dto.LoteProveedorRequest;
import com.app.inventario.dto.LoteProveedorResponse;

import java.util.List;

public interface LoteProveedorService {

    List<LoteProveedorResponse> listar();

    LoteProveedorResponse obtenerPorId(Long id);

    List<LoteProveedorResponse> listarPorProveedor(Long proveedorId);

    List<LoteProveedorResponse> listarPorProducto(Long productoId);

    LoteProveedorResponse crear(LoteProveedorRequest request);

    LoteProveedorResponse actualizar(Long id, LoteProveedorRequest request);

    void eliminar(Long id);
}
