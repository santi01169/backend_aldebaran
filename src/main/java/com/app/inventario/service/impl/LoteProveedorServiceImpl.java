package com.app.inventario.service.impl;

import com.app.inventario.dto.LoteProveedorRequest;
import com.app.inventario.dto.LoteProveedorResponse;
import com.app.inventario.entities.LoteProveedorEntity;
import com.app.inventario.entities.ProductoEntity;
import com.app.inventario.entities.ProveedorEntity;
import com.app.inventario.repository.LoteProveedorRepository;
import com.app.inventario.repository.ProductoRepository;
import com.app.inventario.repository.ProveedorRepository;
import com.app.inventario.service.LoteProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoteProveedorServiceImpl implements LoteProveedorService {

    private final LoteProveedorRepository loteRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<LoteProveedorResponse> listar() {
        return loteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LoteProveedorResponse obtenerPorId(Long id) {
        LoteProveedorEntity entity = loteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lote no encontrado"));
        return toResponse(entity);
    }

    @Override
    public List<LoteProveedorResponse> listarPorProveedor(Long proveedorId) {
        return loteRepository.findByProveedor_Id(proveedorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<LoteProveedorResponse> listarPorProducto(Long productoId) {
        return loteRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LoteProveedorResponse crear(LoteProveedorRequest request) {
        ProveedorEntity proveedor = proveedorRepository.findById(request.proveedorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        ProductoEntity producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producto no encontrado"));

        LoteProveedorEntity entity = LoteProveedorEntity.builder()
                .numeroLote(request.numeroLote())
                .fechaIngreso(request.fechaIngreso())
                .fechaVencimiento(request.fechaVencimiento())
                .cantidadInicial(request.cantidadInicial())
                .proveedor(proveedor)
                .producto(producto)
                .build();

        LoteProveedorEntity guardado = loteRepository.save(entity);
        return toResponse(guardado);
    }

    @Override
    public LoteProveedorResponse actualizar(Long id, LoteProveedorRequest request) {
        LoteProveedorEntity entity = loteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lote no encontrado"));

        ProveedorEntity proveedor = proveedorRepository.findById(request.proveedorId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        ProductoEntity producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producto no encontrado"));

        entity.setNumeroLote(request.numeroLote());
        entity.setFechaIngreso(request.fechaIngreso());
        entity.setFechaVencimiento(request.fechaVencimiento());
        entity.setCantidadInicial(request.cantidadInicial());
        entity.setProveedor(proveedor);
        entity.setProducto(producto);

        LoteProveedorEntity actualizado = loteRepository.save(entity);
        return toResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        LoteProveedorEntity entity = loteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lote no encontrado"));

        loteRepository.delete(entity);
    }

    // ================== MAPPER ==================

    private LoteProveedorResponse toResponse(LoteProveedorEntity e) {
        return new LoteProveedorResponse(
                e.getId(),
                e.getNumeroLote(),
                e.getFechaIngreso(),
                e.getFechaVencimiento(),
                e.getCantidadInicial(),
                e.getProveedor() != null ? e.getProveedor().getId() : null,
                e.getProveedor() != null ? e.getProveedor().getNombre() : null,   // ajusta getNombre() si tu campo se llama distinto
                e.getProducto() != null ? e.getProducto().getId() : null,
                e.getProducto() != null ? e.getProducto().getNombre() : null      // aquí también, según tu ProductoEntity
        );
    }
}
