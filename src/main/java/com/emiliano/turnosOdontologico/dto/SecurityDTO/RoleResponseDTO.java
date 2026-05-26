package com.emiliano.turnosOdontologico.dto.SecurityDTO;

import java.util.List;

public record RoleResponseDTO(
        Long id,
        String role,
        List<PermissionResponseDTO> permissions
) {
}
