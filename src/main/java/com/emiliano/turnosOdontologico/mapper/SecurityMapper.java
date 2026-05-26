package com.emiliano.turnosOdontologico.mapper;

import com.emiliano.turnosOdontologico.dto.SecurityDTO.PermissionRequestDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.PermissionResponseDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.RoleRequestDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.RoleResponseDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecCreateRequestDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecResponseDTO;
import com.emiliano.turnosOdontologico.dto.SecurityDTO.UserSecUpdateRequestDTO;
import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SecurityMapper {

    public Permission toPermissionEntity(PermissionRequestDTO dto) {
        Permission permission = new Permission();
        permission.setPermissionName(dto.permissionName());
        return permission;
    }

    public PermissionResponseDTO toPermissionResponseDTO(Permission permission) {
        return new PermissionResponseDTO(
                permission.getId(),
                permission.getPermissionName()
        );
    }

    public void updatePermissionFromDTO(Permission permission, PermissionRequestDTO dto) {
        permission.setPermissionName(dto.permissionName());
    }

    public Role toRoleEntity(RoleRequestDTO dto) {
        Role role = new Role();
        role.setRole(dto.role());
        return role;
    }

    public RoleResponseDTO toRoleResponseDTO(Role role) {
        List<PermissionResponseDTO> permissions = role.getPermissionsList().stream()
                .sorted(Comparator.comparing(Permission::getId))
                .map(this::toPermissionResponseDTO)
                .toList();

        return new RoleResponseDTO(
                role.getId(),
                role.getRole(),
                permissions
        );
    }

    public void updateRoleFromDTO(Role role, RoleRequestDTO dto) {
        role.setRole(dto.role());
    }

    public UserSec toUserEntity(UserSecCreateRequestDTO dto, String encryptedPassword) {
        UserSec user = new UserSec();
        user.setUsername(dto.username());
        user.setPassword(encryptedPassword);
        user.setEnabled(dto.enabled());
        user.setAccountNotExpired(dto.accountNotExpired());
        user.setAccountNotLocked(dto.accountNotLocked());
        user.setCredentialNotExpired(dto.credentialNotExpired());
        return user;
    }

    public UserSecResponseDTO toUserResponseDTO(UserSec user) {
        List<RoleResponseDTO> roles = user.getRoleList().stream()
                .sorted(Comparator.comparing(Role::getId))
                .map(this::toRoleResponseDTO)
                .toList();

        return new UserSecResponseDTO(
                user.getId(),
                user.getUsername(),
                user.isEnabled(),
                user.isAccountNotExpired(),
                user.isAccountNotLocked(),
                user.isCredentialNotExpired(),
                roles
        );
    }

    public void updateUserFromDTO(UserSec user, UserSecUpdateRequestDTO dto) {
        user.setUsername(dto.username());
        user.setEnabled(dto.enabled());
        user.setAccountNotExpired(dto.accountNotExpired());
        user.setAccountNotLocked(dto.accountNotLocked());
        user.setCredentialNotExpired(dto.credentialNotExpired());
    }
}
