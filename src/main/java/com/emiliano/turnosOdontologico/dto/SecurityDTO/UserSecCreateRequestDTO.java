package com.emiliano.turnosOdontologico.dto.SecurityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserSecCreateRequestDTO(
        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede superar los 50 caracteres")
        String username,

        @NotBlank(message = "La password es obligatoria")
        @Size(min = 6, max = 100, message = "La password debe tener entre 6 y 100 caracteres")
        String password,

        boolean enabled,
        boolean accountNotExpired,
        boolean accountNotLocked,
        boolean credentialNotExpired,

        @NotEmpty(message = "Debe indicar al menos un rol")
        Set<Long> roleIds
) {
}
