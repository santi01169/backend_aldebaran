package com.app.inventario.repository;

import com.app.inventario.entities.DetalleRecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRecetaRepository extends JpaRepository<DetalleRecetaEntity, Long> {

    // Por si necesitas consultar los ingredientes de una receta específica
    List<DetalleRecetaEntity> findByRecetaId(Long recetaId);

    // Por si necesitas saber en qué recetas se usa un insumo específico
    List<DetalleRecetaEntity> findByInsumoId(Long insumoId);
}

