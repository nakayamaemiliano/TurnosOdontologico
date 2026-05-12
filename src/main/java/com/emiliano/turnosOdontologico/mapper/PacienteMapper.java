package com.emiliano.turnosOdontologico.mapper;

import com.emiliano.turnosOdontologico.entity.Paciente;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.nombre());
        paciente.setApellido(dto.apellido());
        paciente.setDni(dto.dni());
        paciente.setEmail(dto.email());
        paciente.setTelefono(dto.telefono());
        return paciente;
    }

    public PacienteResponseDTO toResponseDTO(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getEmail(),
                paciente.getTelefono()
        );
    }

    public void updateEntityFromDTO(Paciente paciente, PacienteRequestDTO dto) {
        paciente.setNombre(dto.nombre());
        paciente.setApellido(dto.apellido());
        paciente.setDni(dto.dni());
        paciente.setEmail(dto.email());
        paciente.setTelefono(dto.telefono());
    }



}
