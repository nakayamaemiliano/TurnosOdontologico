package com.emiliano.turnosOdontologico.security.config;

import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import com.emiliano.turnosOdontologico.security.repository.PermissionRepository;
import com.emiliano.turnosOdontologico.security.repository.RoleRepository;
import com.emiliano.turnosOdontologico.security.repository.UserSecRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SecurityDataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserSecRepository userSecRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.admin.username:demo_admin}")
    private String adminUsername;

    @Value("${app.seed.admin.password:admin123}")
    private String adminPassword;

    public SecurityDataInitializer(PermissionRepository permissionRepository,
                                   RoleRepository roleRepository,
                                   UserSecRepository userSecRepository,
                                   PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userSecRepository = userSecRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        Permission read = getOrCreatePermission("READ");
        Permission create = getOrCreatePermission("CREATE");
        Permission update = getOrCreatePermission("UPDATE");
        Permission delete = getOrCreatePermission("DELETE");

        Role adminRole = getOrCreateRole("ADMIN", Set.of(read, create, update, delete));
        getOrCreateRole("USER", Set.of(read));

        userSecRepository.findUserEntityByUsername(adminUsername)
                .orElseGet(() -> createAdminUser(adminRole));
    }

    private Permission getOrCreatePermission(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setPermissionName(permissionName);
                    return permissionRepository.save(permission);
                });
    }

    private Role getOrCreateRole(String roleName, Set<Permission> permissions) {
        Role role = roleRepository.findByRole(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleName);
                    return newRole;
                });

        role.setPermissionsList(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    private UserSec createAdminUser(Role adminRole) {
        UserSec admin = new UserSec();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        admin.setAccountNotExpired(true);
        admin.setAccountNotLocked(true);
        admin.setCredentialNotExpired(true);
        admin.setRoleList(new HashSet<>(List.of(adminRole)));
        return userSecRepository.save(admin);
    }
}
