package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.service.IPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Permisos", description = "Operaciones para gestionar permisos de seguridad")
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @Operation(summary = "Listar permisos", description = "Devuelve todos los permisos del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions(){
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @Operation(summary = "Buscar permiso por ID", description = "Devuelve un permiso segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear permiso", description = "Registra un nuevo permiso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission newPermission = permissionService.save(permission);
        return ResponseEntity.ok(newPermission);
    }

    @Operation(summary = "Actualizar permiso", description = "Actualiza el nombre de un permiso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
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

    @Operation(summary = "Eliminar permiso", description = "Elimina un permiso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
    })
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
