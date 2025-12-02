package com.app.inventario.service;

import com.app.inventario.dto.MovimientoStockRequest;
import com.app.inventario.dto.MovimientoStockResponse;
import com.app.inventario.dto.StockRequest;
import com.app.inventario.dto.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {

    List<StockResponse> listar();

    StockResponse obtenerPorId(Integer id);

    StockResponse obtenerPorInsumo(Long insumoId);

    StockResponse crearOActualizar(StockRequest request);

    StockResponse actualizar(Integer id, StockRequest request);

    void eliminar(Integer id);

    StockResponse registrarEntrada(Integer stockId, MovimientoStockRequest request);

    StockResponse registrarSalida(Integer stockId, MovimientoStockRequest request);

    List<MovimientoStockResponse> listarMovimientos(Integer stockId);

    Page<StockResponse> filtrar(Long insumoId,
                                String ubicacion,
                                Integer cantidadMin,
                                Integer cantidadMax,
                                Boolean sinStock,
                                Pageable pageable);
}
