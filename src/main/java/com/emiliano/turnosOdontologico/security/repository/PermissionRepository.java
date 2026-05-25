package com.emiliano.turnosOdontologico.security.repository;

import com.emiliano.turnosOdontologico.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
