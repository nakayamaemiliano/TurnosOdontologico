package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.OdontologoMasSolicitadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.ResumenTurnosPorEstadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoRequestDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ITurnoService {
    TurnoResponseDTO crearTurno(TurnoRequestDTO dto);

    List<TurnoResponseDTO> listarTurnos();

    TurnoResponseDTO buscarTurnoPorId(Long id);

    TurnoResponseDTO actualizarTurno(Long id, TurnoRequestDTO dto);

    void eliminarTurno(Long id);

    List<TurnoResponseDTO> obtenerTurnosPorFecha(LocalDate fecha);

    List<TurnoResponseDTO> obtenerTurnosPorOdontologo(Long idOdontologo);

    List<TurnoResponseDTO> obtenerTurnosPorPaciente(Long idPaciente);

    TurnoResponseDTO confirmarTurno(Long id);

    TurnoResponseDTO cancelarTurno(Long id);

    ResumenTurnosPorEstadoDTO obtenerResumenPorFecha(LocalDate fecha);

    OdontologoMasSolicitadoDTO obtenerOdontologoMasSolicitado();
}
