package com.emiliano.turnosOdontologico.dto.SecurityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RoleRequestDTO(
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 50, message = "El nombre del rol no puede superar los 50 caracteres")
        String role,

        @NotEmpty(message = "Debe indicar al menos un permiso")
        Set<Long> permissionIds
) {
}
