package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.service.IPermissionService;
import com.emiliano.turnosOdontologico.security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permiService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Set<Permission> permiList = new HashSet<Permission>();
        Permission readPermission;

        // Recuperar la Permission/s por su ID
        for (Permission per : role.getPermissionsList()) {
            readPermission = permiService.findById(per.getId()).orElse(null);
            if (readPermission != null) {
                //si encuentro, guardo en la lista
                permiList.add(readPermission);
            }
        }

        role.setPermissionsList(permiList);
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(
            @PathVariable Long id,
            @RequestBody Role roleDetails) {

        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Role role = roleOptional.get();

        role.setRole(roleDetails.getRole());

        Set<Permission> permiList = new HashSet<>();
        Permission readPermission;

        for (Permission per : roleDetails.getPermissionsList()) {
            readPermission = permiService.findById(per.getId()).orElse(null);

            if (readPermission != null) {
                permiList.add(readPermission);
            }
        }

        role.setPermissionsList(permiList);

        Role updatedRole = roleService.save(role);

        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        roleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
