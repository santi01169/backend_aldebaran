package com.app.inventario.service.impl;

import com.app.inventario.dto.ProductoRequest;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.entities.CategoriaEntity;
import com.app.inventario.entities.ProductoEntity;
import com.app.inventario.entities.UnidadMedidaEntity;
import com.app.inventario.repository.CategoriaRepository;
import com.app.inventario.repository.ProductoRepository;
import com.app.inventario.repository.UnidadMedidaRepository;
import com.app.inventario.service.ProductoService;
import com.app.inventario.spec.ProductoSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;
    private final UnidadMedidaRepository unidadMedidaRepo;

    // ============================================================
    // CREATE
    // ============================================================
    @Override
    public ProductoResponse create(ProductoRequest request) {

        ProductoEntity entity = new ProductoEntity();
        apply(entity, request);

        ProductoEntity saved = repo.save(entity);

        return toResponse(saved);
    }

    // ============================================================
    // LISTAR
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        ProductoEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        return toResponse(e);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest r) {

        ProductoEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));

        apply(e, r);

        ProductoEntity saved = repo.save(e);

        return toResponse(saved);
    }

    // ============================================================
    // DELETE
    // ============================================================
    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("Producto no encontrado: " + id);

        repo.deleteById(id);
    }

    // ============================================================
    // FILTRAR (con Specifications)
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponse> filtrar(String nombre,
                                          Long categoriaId,
                                          Long unidadMedidaId,
                                          Integer stockMinimo,
                                          Pageable pageable) {

        Specification<ProductoEntity> spec = (root, query, cb) -> null;

        spec = spec
                .and(ProductoSpecifications.nombreContiene(nombre))
                .and(ProductoSpecifications.tieneCategoria(categoriaId))
                .and(ProductoSpecifications.tieneUnidadMedida(unidadMedidaId))
                .and(ProductoSpecifications.stockMinimoMayorOIgual(stockMinimo));

        return repo.findAll(spec, pageable)
                .map(this::toResponse);
    }

    // ============================================================
    // Asignar datos desde el request
    // ============================================================
    private void apply(ProductoEntity e, ProductoRequest r) {
        e.setNombre(r.nombre());
        e.setDescripcion(r.descripcion());
        e.setStockMinimo(r.stockMinimo());

        if (r.categoriaId() != null) {
            CategoriaEntity categoria = categoriaRepo.findById(r.categoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + r.categoriaId()));
            e.setCategoria(categoria);
        }

        if (r.unidadMedidaId() != null) {
            UnidadMedidaEntity unidadMedida = unidadMedidaRepo.findById(r.unidadMedidaId())
                    .orElseThrow(() -> new IllegalArgumentException("Unidad de medida no encontrada: " + r.unidadMedidaId()));
            e.setUnidadMedida(unidadMedida);
        }
    }

    // ============================================================
    // Mapear a DTO
    // ============================================================
    private ProductoResponse toResponse(ProductoEntity e) {
        return new ProductoResponse(
                e.getId(),
                e.getNombre(),
                e.getDescripcion(),
                e.getStockMinimo(),
                e.getCategoria() != null ? e.getCategoria().getId() : null,
                e.getUnidadMedida() != null ? e.getUnidadMedida().getId() : null,
                e.getCategoria() != null ? e.getCategoria().getNombre() : null,
                e.getUnidadMedida() != null ? e.getUnidadMedida().getNombre() : null
        );
    }
}
