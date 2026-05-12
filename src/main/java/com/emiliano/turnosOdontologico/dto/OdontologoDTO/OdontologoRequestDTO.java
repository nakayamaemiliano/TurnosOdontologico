package com.emiliano.turnosOdontologico.dto.OdontologoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OdontologoRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 30, message = "El apellido no puede superar los 30 caracteres")
        String apellido,

        @NotBlank(message = "La matrícula es obligatoria")
        @Size(max = 30, message = "La matrícula no puede superar los 30 caracteres")
        String matricula,

        @NotBlank(message = "La especialidad es obligatoria")
        @Size(max = 40, message = "La especialidad no puede superar los 40 caracteres")
        String especialidad

) {
}
