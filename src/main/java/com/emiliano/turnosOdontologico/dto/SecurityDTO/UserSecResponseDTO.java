package com.emiliano.turnosOdontologico.dto.SecurityDTO;

import java.util.List;

public record UserSecResponseDTO(
        Long id,
        String username,
        boolean enabled,
        boolean accountNotExpired,
        boolean accountNotLocked,
        boolean credentialNotExpired,
        List<RoleResponseDTO> roles
) {
}
