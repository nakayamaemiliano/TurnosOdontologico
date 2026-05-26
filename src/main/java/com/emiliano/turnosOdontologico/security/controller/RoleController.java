package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.dto.SecurityDTO.RoleRequestDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.RoleResponseDTO;
import com.emiliano.turnosOdontologico.mapper.SecurityMapper;
import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.service.IPermissionService;
import com.emiliano.turnosOdontologico.security.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Roles", description = "Operaciones para gestionar roles de seguridad")
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permiService;

    @Autowired
    private SecurityMapper securityMapper;

    @Operation(summary = "Listar roles", description = "Devuelve todos los roles del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.findAll().stream()
                .map(securityMapper::toRoleResponseDTO)
                .toList();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Buscar rol por ID", description = "Devuelve un rol segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role
                .map(securityMapper::toRoleResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear rol", description = "Registra un rol y le asigna permisos existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        Set<Permission> permiList = findPermissionsByIds(roleRequestDTO.permissionIds());

        if (permiList.size() != roleRequestDTO.permissionIds().size()) {
            return ResponseEntity.badRequest().build();
        }

        Role role = securityMapper.toRoleEntity(roleRequestDTO);
        role.setPermissionsList(permiList);
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(securityMapper.toRoleResponseDTO(newRole));
    }

    @Operation(summary = "Actualizar rol", description = "Actualiza el nombre y permisos de un rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @RequestBody @Valid RoleRequestDTO roleRequestDTO) {

        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Role role = roleOptional.get();

        Set<Permission> permiList = findPermissionsByIds(roleRequestDTO.permissionIds());

        if (permiList.size() != roleRequestDTO.permissionIds().size()) {
            return ResponseEntity.badRequest().build();
        }

        securityMapper.updateRoleFromDTO(role, roleRequestDTO);
        role.setPermissionsList(permiList);

        Role updatedRole = roleService.save(role);

        return ResponseEntity.ok(securityMapper.toRoleResponseDTO(updatedRole));
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        roleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private Set<Permission> findPermissionsByIds(Set<Long> permissionIds) {
        return permissionIds.stream()
                .map(id -> permiService.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
