package com.app.inventario.service;

import com.app.inventario.dto.ProveedorRequest;
import com.app.inventario.dto.ProveedorResponse;

import java.util.List;

public interface ProveedorService {

    List<ProveedorResponse> listar();

    ProveedorResponse obtenerPorId(Long id);

    ProveedorResponse crear(ProveedorRequest request);

    ProveedorResponse actualizar(Long id, ProveedorRequest request);

    void eliminar(Long id);

    ProveedorResponse actualizar(Integer id, ProveedorRequest request);

    void eliminar(Integer id);
}

