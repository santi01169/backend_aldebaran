package com.app.inventario.controller;

import com.app.inventario.dto.ProductoRequest;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    // LISTAR (simple, sin filtros)
    // ============================
    @GetMapping
    public List<ProductoResponse> listar() {
        return service.listar();
    }

    // ============================
    // LISTAR CON FILTROS AVANZADOS
    // ============================
    @GetMapping("/filtro")
    public ResponseEntity<Page<ProductoResponse>> filtrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long unidadMedidaId,
            @RequestParam(required = false) Integer stockMinimo,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable
    ) {
        Page<ProductoResponse> pagina = service.filtrar(
                nombre, categoriaId, unidadMedidaId, stockMinimo, pageable
        );
        return ResponseEntity.ok(pagina);
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
