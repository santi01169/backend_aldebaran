package com.app.inventario.repository;

import com.app.inventario.entities.RecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecetaRepository extends JpaRepository<RecetaEntity, Long>,
        JpaSpecificationExecutor<RecetaEntity> {
}
