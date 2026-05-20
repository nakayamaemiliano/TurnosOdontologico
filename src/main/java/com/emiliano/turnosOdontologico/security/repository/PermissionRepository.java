package com.emiliano.turnosOdontologico.security.repository;

import com.emiliano.turnosOdontologico.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
