package com.emiliano.turnosOdontologico.mapper;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoResponseDTO;
import com.emiliano.turnosOdontologico.entity.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapper {
    public TurnoResponseDTO toResponseDTO(Turno turno) {
        return new TurnoResponseDTO(
                turno.getId(),
                turno.getFecha(),
                turno.getHora(),
                turno.getEstado(),

                turno.getPaciente().getId(),
                turno.getPaciente().getNombre(),
                turno.getPaciente().getApellido(),

                turno.getOdontologo().getId(),
                turno.getOdontologo().getNombre(),
                turno.getOdontologo().getApellido(),
                turno.getOdontologo().getEspecialidad()
        );
    }
}
