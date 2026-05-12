package com.emiliano.turnosOdontologico.dto.UsuarioDTO;

import com.emiliano.turnosOdontologico.enums.Rol;

public record UsuarioResponseDTO(
        Long id,
        String username,
        Rol rol
) {
}
