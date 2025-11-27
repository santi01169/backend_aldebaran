package com.app.inventario.repository;

import com.app.inventario.entities.RecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepository extends JpaRepository<RecetaEntity, Long> {
    // ELIMINADO el método findByProductoFinalIdAndActivoTrue
    // porque RecetaEntity NO tiene el campo productoFinalId
}
