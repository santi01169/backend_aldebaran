package com.app.inventario.controller;

import com.app.inventario.dto.ProductoRequest;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductoService service;

    public ProductController(ProductoService service) {
        this.service = service;
    }

    // ============================
    // LISTAR
    // ============================
    @GetMapping
    public List<ProductoResponse> listar() {
        return service.listar();
    }

    // ============================
    // OBTENER POR ID
    // ============================
    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // ============================
    // CREAR
    // ============================
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse creado = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/productos/" + creado.id()))
                .body(creado);
    }

    // ============================
    // ACTUALIZAR
    // ============================
    @PutMapping("/{id}")
    public ProductoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request
    ) {
        return service.actualizar(id, request);
    }

    // ============================
    // ELIMINAR
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
