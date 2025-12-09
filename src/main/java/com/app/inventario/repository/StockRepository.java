package com.app.inventario.repository;

import com.app.inventario.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Integer>,
        JpaSpecificationExecutor<StockEntity> {

    // ⚠️ SOLO usar si NO existen múltiples lotes
    Optional<StockEntity> findByInsumo_Id(Long insumoId);

    // 🔥 Nuevo: para buscar lote específico
    Optional<StockEntity> findByInsumo_IdAndLoteProveedor_Id(Long insumoId, Long loteProveedorId);

    // 🔥 Nuevo: FEFO (ordenar por fecha de vencimiento ascendente)
    List<StockEntity> findByInsumo_IdOrderByLoteProveedor_FechaVencimientoAsc(Long insumoId);

    @Query("SELECT s FROM StockEntity s " +
            "WHERE s.cantidadActual > 0 " +
            "AND s.cantidadActual <= s.insumo.stockMinimo " +
            "ORDER BY s.cantidadActual ASC")
    List<StockEntity> findProductosConStockBajo();

    // Nuevo: listar solo stocks con cantidad > 0
    List<StockEntity> findByCantidadActualGreaterThan(Integer cantidad);

}
