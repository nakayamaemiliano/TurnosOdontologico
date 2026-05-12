package com.emiliano.turnosOdontologico.dto.TurnoDTO;

public record OdontologoMasSolicitadoDTO(
        Long idOdontologo,
        String nombre,
        String apellido,
        String especialidad,
        long cantidadTurnosAtendidos
) {
}
