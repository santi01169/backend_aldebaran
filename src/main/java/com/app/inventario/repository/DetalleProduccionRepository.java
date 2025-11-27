package com.app.inventario.repository;

import com.app.inventario.entities.DetalleProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleProduccionRepository extends JpaRepository<DetalleProduccionEntity, Long> {
}