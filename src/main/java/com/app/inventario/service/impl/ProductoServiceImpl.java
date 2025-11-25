package com.app.inventario.service.impl;

import com.app.inventario.dto.ProductoRequest;
import com.app.inventario.dto.ProductoResponse;
import com.app.inventario.entities.ProductoEntity;
import com.app.inventario.entities.CategoriaEntity;
import com.app.inventario.entities.UnidadMedidaEntity;
import com.app.inventario.repository.ProductoRepository;
import com.app.inventario.repository.CategoriaRepository;
import com.app.inventario.repository.UnidadMedidaRepository;
import com.app.inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;  // ✅ AGREGAR
    private final UnidadMedidaRepository unidadMedidaRepo;  // ✅ AGREGAR

    // ============================================================
    // CREATE
    // ============================================================
    @Override
    public ProductoResponse create(ProductoRequest request) {

        ProductoEntity entity = new ProductoEntity();
        apply(entity, request);

        // guardar
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

        // guardar cambios
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
    // ✅ MÉTODO MODIFICADO: Buscar y asignar entidades completas
    // ============================================================
    private void apply(ProductoEntity e, ProductoRequest r) {
        e.setNombre(r.nombre());
        e.setDescripcion(r.descripcion());
        e.setStockMinimo(r.stockMinimo());

        // ✅ Buscar y asignar la Categoría completa
        if (r.categoriaId() != null) {
            CategoriaEntity categoria = categoriaRepo.findById(r.categoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + r.categoriaId()));
            e.setCategoria(categoria);
        }

        // ✅ Buscar y asignar la Unidad de Medida completa
        if (r.unidadMedidaId() != null) {
            UnidadMedidaEntity unidadMedida = unidadMedidaRepo.findById(r.unidadMedidaId())
                    .orElseThrow(() -> new IllegalArgumentException("Unidad de medida no encontrada: " + r.unidadMedidaId()));
            e.setUnidadMedida(unidadMedida);
        }
    }


    // ============================================================
    // ✅ MÉTODO MODIFICADO: Ahora accede a los IDs desde las relaciones
    // ============================================================
    private ProductoResponse toResponse(ProductoEntity e) {
        return new ProductoResponse(
                e.getId(),
                e.getNombre(),
                e.getDescripcion(),
                e.getStockMinimo(),
                e.getCategoria() != null ? e.getCategoria().getId() : null,  // ✅ Cambio
                e.getUnidadMedida() != null ? e.getUnidadMedida().getId() : null,  // ✅ Cambio
                e.getCategoria() != null ? e.getCategoria().getNombre() : null,
                e.getUnidadMedida() != null ? e.getUnidadMedida().getNombre() : null
        );
    }

}