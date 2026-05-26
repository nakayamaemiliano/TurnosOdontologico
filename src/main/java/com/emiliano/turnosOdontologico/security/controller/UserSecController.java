package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecCreateRequestDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecResponseDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecUpdateRequestDTO;
import com.emiliano.turnosOdontologico.mapper.SecurityMapper;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import com.emiliano.turnosOdontologico.security.service.IRoleService;
import com.emiliano.turnosOdontologico.security.service.UserSecService;
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

@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios de seguridad")
@RestController
@RequestMapping("/users")
public class UserSecController {
    @Autowired
    private UserSecService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private SecurityMapper securityMapper;

    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @GetMapping
    public ResponseEntity<List<UserSecResponseDTO>> getAllUsers() {
        List<UserSecResponseDTO> users = userService.findAll().stream()
                .map(securityMapper::toUserResponseDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuario por ID", description = "Devuelve un usuario segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserSecResponseDTO> getUserById(@PathVariable Long id) {
        Optional<UserSec> user = userService.findById(id);
        return user
                .map(securityMapper::toUserResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear usuario", description = "Registra un usuario y le asigna roles existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @PostMapping
    public ResponseEntity<UserSecResponseDTO> createUser(@RequestBody @Valid UserSecCreateRequestDTO userRequestDTO) {
        Set<Role> roleList = findRolesByIds(userRequestDTO.roleIds());

        if (roleList.size() != userRequestDTO.roleIds().size()) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = userService.encriptPassword(userRequestDTO.password());
        UserSec userSec = securityMapper.toUserEntity(userRequestDTO, encryptedPassword);
        userSec.setRoleList(roleList);

        UserSec newUser = userService.save(userSec);
        return ResponseEntity.ok(securityMapper.toUserResponseDTO(newUser));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza datos, estado y roles de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserSecResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserSecUpdateRequestDTO userRequestDTO) {

        Optional<UserSec> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<Role> roleList = findRolesByIds(userRequestDTO.roleIds());

        if (roleList.size() != userRequestDTO.roleIds().size()) {
            return ResponseEntity.badRequest().build();
        }

        UserSec user = userOptional.get();
        securityMapper.updateUserFromDTO(user, userRequestDTO);

        if (userRequestDTO.password() != null && !userRequestDTO.password().isBlank()) {
            user.setPassword(userService.encriptPassword(userRequestDTO.password()));
        }

        user.setRoleList(roleList);

        UserSec updatedUser = userService.save(user);
        return ResponseEntity.ok(securityMapper.toUserResponseDTO(updatedUser));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<UserSec> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private Set<Role> findRolesByIds(Set<Long> roleIds) {
        return roleIds.stream()
                .map(id -> roleService.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
