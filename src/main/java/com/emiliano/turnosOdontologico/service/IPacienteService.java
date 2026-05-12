package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;

import java.util.List;

public interface IPacienteService {
    List<PacienteResponseDTO>listarPacientes();

    PacienteResponseDTO crearPaciente(PacienteRequestDTO dto);

    PacienteResponseDTO buscarPacientePorId(Long id);

    PacienteResponseDTO actualizarPaciente(Long id, PacienteRequestDTO dto);

    void eliminarPaciente(Long id);
}
