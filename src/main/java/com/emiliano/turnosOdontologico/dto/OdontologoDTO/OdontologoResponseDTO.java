package com.emiliano.turnosOdontologico.dto.OdontologoDTO;

public record OdontologoResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String matricula,
        String especialidad
) {
}
