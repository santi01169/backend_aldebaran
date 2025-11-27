package com.app.inventario.controller;

import com.app.inventario.dto.*;
import com.app.inventario.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<RecetaResponse>> listar() {
        return ResponseEntity.ok(recetaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recetaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RecetaResponse> crear(@Valid @RequestBody RecetaRequest request) {
        RecetaResponse creada = recetaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaRequest request
    ) {
        RecetaResponse actualizada = recetaService.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============ NUEVOS ENDPOINTS (IGUAL QUE PRODUCCIÓN) ============

    // Agregar un ingrediente a la receta
    @PostMapping("/{recetaId}/agregar-ingrediente")
    public ResponseEntity<RecetaResponse> agregarIngrediente(
            @PathVariable Long recetaId,
            @Valid @RequestBody DetalleRecetaRequest detalle
    ) {
        RecetaResponse actualizada = recetaService.agregarIngrediente(recetaId, detalle);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar un ingrediente de la receta
    @DeleteMapping("/ingredientes/{ingredienteId}")
    public ResponseEntity<Void> eliminarIngrediente(@PathVariable Long ingredienteId) {
        recetaService.eliminarIngrediente(ingredienteId);
        return ResponseEntity.noContent().build();
    }

    // Obtener ingredientes de una receta (para cargar en producción)
    @GetMapping("/{recetaId}/detalles")
    public ResponseEntity<List<DetalleProduccionRequest>> obtenerDetalles(
            @PathVariable Long recetaId
    ) {
        List<DetalleProduccionRequest> materiales =
                recetaService.obtenerDetallesDeReceta(recetaId);
        return ResponseEntity.ok(materiales);
    }
}
