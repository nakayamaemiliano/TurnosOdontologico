package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;
import com.emiliano.turnosOdontologico.entity.Odontologo;
import com.emiliano.turnosOdontologico.exception.ResourceNotFoundException;
import com.emiliano.turnosOdontologico.mapper.OdontologoMapper;
import com.emiliano.turnosOdontologico.repository.OdontologoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class OdontologoService implements IOdontologoService{

    @Autowired
    private OdontologoRepository odontologoRepository;
    @Autowired
    private OdontologoMapper odontologoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OdontologoResponseDTO> listarOdontologos() {
        return odontologoRepository.findAll()
                .stream()
                .map(odontologoMapper::toResponseDTO)
                .toList();

    }

    @Override
    public OdontologoResponseDTO crearOdontologo(OdontologoRequestDTO dto) {
        Odontologo odontologo = odontologoMapper.toEntity(dto);
        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        return odontologoMapper.toResponseDTO(odontologoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public OdontologoResponseDTO buscarOdontologoPorId(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + id));
        return  odontologoMapper.toResponseDTO(odontologo);
    }

    @Override
    public OdontologoResponseDTO actualizarOdontologo(Long id, OdontologoRequestDTO dto) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + id));

        odontologoMapper.updateEntityFromDto(dto,odontologo);

        Odontologo odontologoActualizado = odontologoRepository.save(odontologo);
        return odontologoMapper.toResponseDTO(odontologoActualizado);
    }

    @Override
    public void eliminarOdontologo(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + id));

        odontologoRepository.delete(odontologo);

    }
}
