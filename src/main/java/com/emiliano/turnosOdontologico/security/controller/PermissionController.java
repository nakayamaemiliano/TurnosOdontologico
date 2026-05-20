package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions(){
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission newPermission = permissionService.save(permission);
        return ResponseEntity.ok(newPermission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(
            @PathVariable Long id,
            @RequestBody Permission permissionDetails) {

        Optional<Permission> permissionOptional = permissionService.findById(id);

        if (permissionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Permission permission = permissionOptional.get();
        permission.setPermissionName(permissionDetails.getPermissionName());

        Permission updatedPermission = permissionService.save(permission);

        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        Optional<Permission> permissionOptional = permissionService.findById(id);

        if (permissionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        permissionService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
