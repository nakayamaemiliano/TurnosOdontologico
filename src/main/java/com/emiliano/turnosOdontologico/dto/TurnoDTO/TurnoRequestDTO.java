package com.emiliano.turnosOdontologico.dto.TurnoDTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoRequestDTO(
        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha del turno no puede ser pasada")
        LocalDate fecha,
        @NotNull(message = "La hora es obligatoria")
        LocalTime hora,
        @NotNull(message = "El id del paciente es obligatorio")
        @Positive(message = "El id del paciente debe ser mayor a 0")
        Long pacienteId,
        @NotNull(message = "El id del odontólogo es obligatorio")
        @Positive(message = "El id del paciente debe ser mayor a 0")
        Long odontologoId

) {
}
