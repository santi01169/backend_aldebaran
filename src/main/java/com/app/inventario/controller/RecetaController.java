package com.app.inventario.controller;

import com.app.inventario.dto.RecetaRequest;
import com.app.inventario.dto.RecetaResponse;
import com.app.inventario.service.RecetaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    // ============================
    // LISTAR SIMPLE
    // ============================
    @GetMapping
    public List<RecetaResponse> listar() {
        return recetaService.listar();
    }

    // ============================
    // LISTAR CON FILTROS
    // ============================
    @GetMapping("/filtro")
    public ResponseEntity<Page<RecetaResponse>> filtrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(required = false) Integer cantidadBaseMinima,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable
    ) {
        Page<RecetaResponse> pagina = recetaService.filtrar(
                nombre, activa, cantidadBaseMinima, pageable);
        return ResponseEntity.ok(pagina);
    }

    // ============================
    // OBTENER POR ID
    // ============================
    @GetMapping("/{id}")
    public RecetaResponse obtener(@PathVariable Long id) {
        return recetaService.obtenerPorId(id);
    }

    // ============================
    // CREAR
    // ============================
    @PostMapping
    public ResponseEntity<RecetaResponse> crear(@Valid @RequestBody RecetaRequest request) {
        RecetaResponse creada = recetaService.crear(request);
        return ResponseEntity
                .created(URI.create("/api/recetas/" + creada.id()))
                .body(creada);
    }

    // ============================
    // ACTUALIZAR
    // ============================
    @PutMapping("/{id}")
    public RecetaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaRequest request
    ) {
        return recetaService.actualizar(id, request);
    }

    // ============================
    // ELIMINAR
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
