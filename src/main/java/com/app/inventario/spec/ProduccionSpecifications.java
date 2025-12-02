package com.app.inventario.spec;

import com.app.inventario.entities.ProduccionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ProduccionSpecifications {

    public static Specification<ProduccionEntity> ordenContiene(String orden) {
        return (root, query, cb) -> {
            if (orden == null || orden.isBlank()) return null;
            String pattern = "%" + orden.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("orden")), pattern);
        };
    }

    public static Specification<ProduccionEntity> estadoEs(String estado) {
        return (root, query, cb) -> {
            if (estado == null || estado.isBlank()) return null;
            return cb.equal(root.get("estado"), estado);
        };
    }

    public static Specification<ProduccionEntity> fechaDesde(LocalDate desde) {
        return (root, query, cb) -> {
            if (desde == null) return null;
            return cb.greaterThanOrEqualTo(root.get("fechaProduccion"), desde);
        };
    }

    public static Specification<ProduccionEntity> fechaHasta(LocalDate hasta) {
        return (root, query, cb) -> {
            if (hasta == null) return null;
            return cb.lessThanOrEqualTo(root.get("fechaProduccion"), hasta);
        };
    }

    public static Specification<ProduccionEntity> usuarioResponsableId(Long usuarioId) {
        return (root, query, cb) -> {
            if (usuarioId == null) return null;
            return cb.equal(root.get("usuarioResponsable").get("id"), usuarioId);
        };
    }
}
