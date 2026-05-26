package com.emiliano.turnosOdontologico.dto.SecurityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequestDTO(
        @NotBlank(message = "El nombre del permiso es obligatorio")
        @Size(max = 50, message = "El nombre del permiso no puede superar los 50 caracteres")
        String permissionName
) {
}
