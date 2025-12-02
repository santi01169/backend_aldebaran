package com.app.inventario.controller;

import com.app.inventario.dto.MovimientoStockRequest;
import com.app.inventario.dto.MovimientoStockResponse;
import com.app.inventario.dto.StockRequest;
import com.app.inventario.dto.StockResponse;
import com.app.inventario.service.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // ============================
    // LISTAR SIMPLE
    // ============================
    @GetMapping
    public List<StockResponse> listar() {
        return stockService.listar();
    }

    // ============================
    // LISTAR CON FILTRO
    // ============================
    @GetMapping("/filtro")
    public ResponseEntity<Page<StockResponse>> filtrar(
            @RequestParam(required = false) Long insumoId,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) Integer cantidadMin,
            @RequestParam(required = false) Integer cantidadMax,
            @RequestParam(required = false) Boolean sinStock,
            @PageableDefault(size = 20, sort = "insumo.nombre") Pageable pageable
    ) {
        Page<StockResponse> pagina = stockService.filtrar(
                insumoId, ubicacion, cantidadMin, cantidadMax, sinStock, pageable);
        return ResponseEntity.ok(pagina);
    }

    // ============================
    // OBTENER POR ID
    // ============================
    @GetMapping("/{id}")
    public StockResponse obtener(@PathVariable Integer id) {
        return stockService.obtenerPorId(id);
    }

    // ============================
    // CREAR / ACTUALIZAR
    // ============================
    @PostMapping
    public ResponseEntity<StockResponse> crear(@Valid @RequestBody StockRequest request) {
        StockResponse resp = stockService.crearOActualizar(request);
        return ResponseEntity
                .created(URI.create("/api/stock/" + resp.id()))
                .body(resp);
    }

    @PutMapping("/{id}")
    public StockResponse actualizar(@PathVariable Integer id,
                                    @Valid @RequestBody StockRequest request) {
        return stockService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        stockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // MOVIMIENTOS
    // ============================
    @PostMapping("/{id}/entradas")
    public StockResponse registrarEntrada(@PathVariable Integer id,
                                          @Valid @RequestBody MovimientoStockRequest request) {
        return stockService.registrarEntrada(id, request);
    }

    @PostMapping("/{id}/salidas")
    public StockResponse registrarSalida(@PathVariable Integer id,
                                         @Valid @RequestBody MovimientoStockRequest request) {
        return stockService.registrarSalida(id, request);
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoStockResponse> listarMovimientos(@PathVariable Integer id) {
        return stockService.listarMovimientos(id);
    }
}
