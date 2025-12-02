package com.app.inventario.service;

import com.app.inventario.dto.ProductoRequest;
import com.app.inventario.dto.ProductoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar();

    ProductoResponse obtener(Long id);

    ProductoResponse create(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);

    Page<ProductoResponse> filtrar(String nombre,
                                   Long categoriaId,
                                   Long unidadMedidaId,
                                   Integer stockMinimo,
                                   Pageable pageable);
}
