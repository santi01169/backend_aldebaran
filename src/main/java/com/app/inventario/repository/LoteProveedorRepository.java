package com.app.inventario.repository;

import com.app.inventario.entities.LoteProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoteProveedorRepository extends JpaRepository<LoteProveedorEntity, Long> {

    List<LoteProveedorEntity> findByProveedor_Id(Long proveedorId);

    List<LoteProveedorEntity> findByProducto_Id(Long productoId);

    List<LoteProveedorEntity> findByFechaVencimientoBetween(LocalDate inicio, LocalDate fin);
}
