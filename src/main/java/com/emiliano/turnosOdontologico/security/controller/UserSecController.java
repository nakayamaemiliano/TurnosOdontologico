package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import com.emiliano.turnosOdontologico.security.service.IRoleService;
import com.emiliano.turnosOdontologico.security.service.UserSecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios de seguridad")
@RestController
@RequestMapping("/users")
public class UserSecController {
    @Autowired
    private UserSecService userService;

    @Autowired
    private IRoleService roleService;

    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @GetMapping
    public ResponseEntity<List<UserSec>> getAllUsers() {
        List<UserSec> users = userService.findAll();
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
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear usuario", description = "Registra un usuario y le asigna roles existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos de administrador")
    })
    @PostMapping
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec userSec) {

        Set<Role> roleList = new HashSet<Role>();
        Role readRole;

        //encriptamos contraseña
        userSec.setPassword(userService.encriptPassword(userSec.getPassword()));

        // Recuperar la Permission/s por su ID
        for (Role role : userSec.getRoleList()){
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null) {
                //si encuentro, guardo en la lista
                roleList.add(readRole);
            }
        }

        if (!roleList.isEmpty()) {
            userSec.setRoleList(roleList);

            UserSec newUser = userService.save(userSec);
            return ResponseEntity.ok(newUser);
        }
        return null;
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
    public ResponseEntity<UserSec> updateUser(
            @PathVariable Long id,
            @RequestBody UserSec userDetails) {

        Optional<UserSec> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserSec user = userOptional.get();

        user.setUsername(userDetails.getUsername());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(userService.encriptPassword(userDetails.getPassword()));
        }

        user.setEnabled(userDetails.isEnabled());
        user.setAccountNotExpired(userDetails.isAccountNotExpired());
        user.setAccountNotLocked(userDetails.isAccountNotLocked());
        user.setCredentialNotExpired(userDetails.isCredentialNotExpired());

        Set<Role> roleList = new HashSet<>();
        Role readRole;

        for (Role role : userDetails.getRoleList()) {
            readRole = roleService.findById(role.getId()).orElse(null);

            if (readRole != null) {
                roleList.add(readRole);
            }
        }

        if (roleList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        user.setRoleList(roleList);

        UserSec updatedUser = userService.save(user);

        return ResponseEntity.ok(updatedUser);
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
}
