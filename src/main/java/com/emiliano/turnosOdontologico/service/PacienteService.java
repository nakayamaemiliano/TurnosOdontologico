package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;
import com.emiliano.turnosOdontologico.entity.Paciente;
import com.emiliano.turnosOdontologico.exception.ResourceNotFoundException;
import com.emiliano.turnosOdontologico.mapper.PacienteMapper;
import com.emiliano.turnosOdontologico.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PacienteService implements IPacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;


    @Override
    public List<PacienteResponseDTO> listarPacientes() {
        return  pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PacienteResponseDTO crearPaciente(PacienteRequestDTO dto) {
        Paciente paciente = pacienteMapper.toEntity(dto);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return pacienteMapper.toResponseDTO(pacienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPacientePorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        return pacienteMapper.toResponseDTO(paciente);
    }

    @Override
    public PacienteResponseDTO actualizarPaciente(Long id, PacienteRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        pacienteMapper.updateEntityFromDTO(paciente, dto);

        Paciente pacienteActualizado = pacienteRepository.save(paciente);
        return pacienteMapper.toResponseDTO(pacienteActualizado);

    }

    @Override
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        pacienteRepository.delete(paciente);
    }


}
