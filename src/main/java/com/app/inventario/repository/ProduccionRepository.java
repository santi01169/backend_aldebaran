package com.app.inventario.repository;

import com.app.inventario.entities.ProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduccionRepository extends JpaRepository<ProduccionEntity, Long>,
        JpaSpecificationExecutor<ProduccionEntity> {

    Optional<ProduccionEntity> findByOrden(String orden);

    List<ProduccionEntity> findByEstado(String estado);
}
