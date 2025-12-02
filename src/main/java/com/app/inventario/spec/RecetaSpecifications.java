package com.app.inventario.spec;

import com.app.inventario.entities.RecetaEntity;
import org.springframework.data.jpa.domain.Specification;

public class RecetaSpecifications {

    public static Specification<RecetaEntity> nombreContiene(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isBlank()) return null;
            String pattern = "%" + nombre.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("nombre")), pattern);
        };
    }

    public static Specification<RecetaEntity> activaEs(Boolean activa) {
        return (root, query, cb) -> {
            if (activa == null) return null;
            return cb.equal(root.get("activo"), activa);
        };
    }

    public static Specification<RecetaEntity> cantidadBaseMinima(Integer cantidad) {
        return (root, query, cb) -> {
            if (cantidad == null) return null;
            return cb.greaterThanOrEqualTo(root.get("cantidadBaseProducida"), cantidad);
        };
    }
}
