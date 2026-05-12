package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;

import java.util.List;

public interface IOdontologoService {

    List<OdontologoResponseDTO> listarOdontologos();

    OdontologoResponseDTO crearOdontologo(OdontologoRequestDTO dto);

    OdontologoResponseDTO buscarOdontologoPorId(Long id );

    OdontologoResponseDTO actualizarOdontologo(Long id,OdontologoRequestDTO dto);

    void eliminarOdontologo(Long id );

}
