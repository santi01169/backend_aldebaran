package com.app.inventario.repository;

import com.app.inventario.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer> {  // ✅ Integer

    Optional<StockEntity> findByInsumo_Id(Long insumoId);

    @Query("SELECT s FROM StockEntity s " +
            "WHERE s.cantidadActual <= s.insumo.stockMinimo " +
            "ORDER BY s.cantidadActual ASC")
    List<StockEntity> findProductosConStockBajo();
}
