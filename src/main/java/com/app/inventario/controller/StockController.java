package com.app.inventario.controller;

import com.app.inventario.dto.MovimientoStockRequest;
import com.app.inventario.dto.MovimientoStockResponse;
import com.app.inventario.dto.StockRequest;
import com.app.inventario.dto.StockResponse;
import com.app.inventario.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public StockResponse obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/insumo/{insumoId}")
    public StockResponse obtenerPorInsumo(@PathVariable Long insumoId) {
        return service.obtenerPorInsumo(insumoId);
    }

    @PostMapping
    public StockResponse crearOActualizar(@Valid @RequestBody StockRequest request) {
        return service.crearOActualizar(request);
    }

    @PostMapping("/{id}/entrada")
    public StockResponse registrarEntrada(
            @PathVariable Integer id,
            @Valid @RequestBody MovimientoStockRequest request
    ) {
        return service.registrarEntrada(id, request);
    }

    @PostMapping("/{id}/salida")
    public StockResponse registrarSalida(
            @PathVariable Integer id,
            @Valid @RequestBody MovimientoStockRequest request
    ) {
        return service.registrarSalida(id, request);
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoStockResponse> listarMovimientos(@PathVariable Integer id) {
        return service.listarMovimientos(id);
    }
}
