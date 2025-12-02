package com.app.inventario.spec;

import com.app.inventario.entities.ProductoEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductoSpecifications {

    public static Specification<ProductoEntity> nombreContiene(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isBlank()) return null;
            String pattern = "%" + nombre.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nombre")), pattern);
        };
    }

    public static Specification<ProductoEntity> tieneCategoria(Long categoriaId) {
        return (root, query, cb) -> {
            if (categoriaId == null) return null;
            return cb.equal(root.get("categoria").get("id"), categoriaId);
        };
    }

    public static Specification<ProductoEntity> tieneUnidadMedida(Long unidadMedidaId) {
        return (root, query, cb) -> {
            if (unidadMedidaId == null) return null;
            return cb.equal(root.get("unidadMedida").get("id"), unidadMedidaId);
        };
    }

    public static Specification<ProductoEntity> stockMinimoMayorOIgual(Integer stockMinimo) {
        return (root, query, cb) -> {
            if (stockMinimo == null) return null;
            return cb.greaterThanOrEqualTo(root.get("stockMinimo"), stockMinimo);
        };
    }
}
