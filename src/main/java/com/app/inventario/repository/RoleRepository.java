package com.app.inventario.repository;

import com.app.inventario.entities.RoleEntity;
import com.app.inventario.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
