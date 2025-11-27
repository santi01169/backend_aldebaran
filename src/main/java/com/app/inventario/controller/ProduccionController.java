package com.app.inventario.controller;

import com.app.inventario.dto.DetalleProduccionRequest;
import com.app.inventario.dto.ProduccionRequest;
import com.app.inventario.dto.ProduccionResponse;
import com.app.inventario.service.ProduccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produccion")
public class ProduccionController {

    private final ProduccionService service;

    public ProduccionController(ProduccionService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProduccionResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduccionResponse> obtenerPorId(@PathVariable Long id) {
        ProduccionResponse produccion = service.obtenerPorId(id);
        return ResponseEntity.ok(produccion);
    }

    @PostMapping
    public ResponseEntity<ProduccionResponse> crear(@Valid @RequestBody ProduccionRequest request) {
        ProduccionResponse creada = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduccionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProduccionRequest request
    ) {
        ProduccionResponse actualizada = service.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/completar")
    public ResponseEntity<ProduccionResponse> completar(@PathVariable Long id) {
        ProduccionResponse completada = service.completarProduccion(id);
        return ResponseEntity.ok(completada);
    }

    // ============ NUEVOS ENDPOINTS PARA RECETAS ============

    @PostMapping("/{produccionId}/cargar-receta/{recetaId}")
    public ResponseEntity<ProduccionResponse> cargarReceta(
            @PathVariable Long produccionId,
            @PathVariable Long recetaId
    ) {
        ProduccionResponse actualizada = service.agregarDetallesDesdeReceta(produccionId, recetaId);
        return ResponseEntity.ok(actualizada);
    }

    @PostMapping("/{produccionId}/agregar-producto")
    public ResponseEntity<ProduccionResponse> agregarProducto(
            @PathVariable Long produccionId,
            @Valid @RequestBody DetalleProduccionRequest detalle
    ) {
        ProduccionResponse actualizada = service.agregarDetalleProducto(produccionId, detalle);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/detalles/{detalleId}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long detalleId) {
        service.eliminarDetalle(detalleId);
        return ResponseEntity.noContent().build();
    }
}
