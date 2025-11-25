package com.app.inventario.controller;

import com.app.inventario.dto.ProveedorRequest;
import com.app.inventario.dto.ProveedorResponse;
import com.app.inventario.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProveedorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProveedorResponse obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ProveedorResponse crear(@Valid @RequestBody ProveedorRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ProveedorResponse actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProveedorRequest request
    ) {
        return service.actualizar(Long.valueOf(id), request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(Long.valueOf(id));
    }
}
