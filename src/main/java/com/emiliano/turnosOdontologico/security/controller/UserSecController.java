package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import com.emiliano.turnosOdontologico.security.service.IRoleService;
import com.emiliano.turnosOdontologico.security.service.UserSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserSecController {
    @Autowired
    private UserSecService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserSec>> getAllUsers() {
        List<UserSec> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

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
