package com.emiliano.turnosOdontologico.dto.TurnoDTO;

import com.emiliano.turnosOdontologico.enums.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoResponseDTO(
        Long id,
        LocalDate fecha,
        LocalTime hora,
        EstadoTurno estado,
        Long pacienteId,
        String nombrePaciente,
        String apellidoPaciente,
        Long odontologoId,
        String nombreOdontologo,
        String apellidoOdontologo,
        String especialidad

) {

}
