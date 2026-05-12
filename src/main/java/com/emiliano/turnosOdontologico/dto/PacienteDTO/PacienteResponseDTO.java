package com.emiliano.turnosOdontologico.dto.PacienteDTO;

public record PacienteResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String email,
        String telefono
) {
}
