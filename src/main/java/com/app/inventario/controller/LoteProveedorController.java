package com.app.inventario.controller;

import com.app.inventario.dto.LoteProveedorRequest;
import com.app.inventario.dto.LoteProveedorResponse;
import com.app.inventario.service.LoteProveedorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
public class LoteProveedorController {

    private final LoteProveedorService service;

    public LoteProveedorController(LoteProveedorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public LoteProveedorResponse obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public List<LoteProveedorResponse> listarPorProveedor(@PathVariable Long proveedorId) {
        return service.listarPorProveedor(proveedorId);
    }

    @GetMapping("/producto/{productoId}")
    public List<LoteProveedorResponse> listarPorProducto(@PathVariable Long productoId) {
        return service.listarPorProducto(productoId);
    }

    @PostMapping
    public LoteProveedorResponse crear(@Valid @RequestBody LoteProveedorRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public LoteProveedorResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteProveedorRequest request
    ) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
