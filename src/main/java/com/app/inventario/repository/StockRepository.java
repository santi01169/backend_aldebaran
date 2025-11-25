package com.app.inventario.repository;

import com.app.inventario.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Integer> {

    // antes estaba con Integer -> cámbialo a Long
    Optional<StockEntity> findByInsumo_Id(Long insumoId);
}
