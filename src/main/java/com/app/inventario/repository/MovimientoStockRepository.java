package com.app.inventario.repository;

import com.app.inventario.entities.MovimientoStockEntity;
import com.app.inventario.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStockEntity, Long> {

    List<MovimientoStockEntity> findByStockOrderByFechaMovimientoDesc(StockEntity stock);
}
