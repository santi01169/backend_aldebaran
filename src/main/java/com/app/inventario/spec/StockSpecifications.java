package com.app.inventario.spec;

import com.app.inventario.entities.StockEntity;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecifications {

    public static Specification<StockEntity> porInsumoId(Long insumoId) {
        return (root, query, cb) -> {
            if (insumoId == null) return null;
            return cb.equal(root.get("insumo").get("id"), insumoId);
        };
    }

    public static Specification<StockEntity> ubicacionContiene(String ubicacion) {
        return (root, query, cb) -> {
            if (ubicacion == null || ubicacion.isBlank()) return null;
            String pattern = "%" + ubicacion.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("ubicacion")), pattern);
        };
    }

    public static Specification<StockEntity> cantidadMinima(Integer min) {
        return (root, query, cb) -> {
            if (min == null) return null;
            return cb.greaterThanOrEqualTo(root.get("cantidadActual"), min);
        };
    }

    public static Specification<StockEntity> cantidadMaxima(Integer max) {
        return (root, query, cb) -> {
            if (max == null) return null;
            return cb.lessThanOrEqualTo(root.get("cantidadActual"), max);
        };
    }

    public static Specification<StockEntity> sinStock(Boolean sinStock) {
        return (root, query, cb) -> {
            if (sinStock == null || !sinStock) return null;
            return cb.equal(root.get("cantidadActual"), 0);
        };
    }
}
