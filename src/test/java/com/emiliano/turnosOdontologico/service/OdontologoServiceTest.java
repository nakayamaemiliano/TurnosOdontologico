package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;
import com.emiliano.turnosOdontologico.entity.Odontologo;
import com.emiliano.turnosOdontologico.exception.ResourceNotFoundException;
import com.emiliano.turnosOdontologico.mapper.OdontologoMapper;
import com.emiliano.turnosOdontologico.repository.OdontologoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OdontologoServiceTest {
    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private OdontologoMapper odontologoMapper;

    @InjectMocks
    private OdontologoService odontologoService;

    @Test
    void listarOdontologos_devuelveLista() {
        Odontologo odontologo = crearOdontologo(1L);
        OdontologoResponseDTO responseDTO = crearResponseDTO(1L);

        when(odontologoRepository.findAll()).thenReturn(List.of(odontologo));
        when(odontologoMapper.toResponseDTO(odontologo)).thenReturn(responseDTO);

        List<OdontologoResponseDTO> resultado = odontologoService.listarOdontologos();

        assertEquals(1, resultado.size());
        assertEquals(responseDTO, resultado.get(0));
        verify(odontologoRepository).findAll();
        verify(odontologoMapper).toResponseDTO(odontologo);
    }

    @Test
    void crearOdontologo_guardaYDevuelveDTO() {
        OdontologoRequestDTO requestDTO = crearRequestDTO();
        Odontologo odontologoSinGuardar = crearOdontologo(null);
        Odontologo odontologoGuardado = crearOdontologo(1L);
        OdontologoResponseDTO responseDTO = crearResponseDTO(1L);

        when(odontologoMapper.toEntity(requestDTO)).thenReturn(odontologoSinGuardar);
        when(odontologoRepository.save(odontologoSinGuardar)).thenReturn(odontologoGuardado);
        when(odontologoMapper.toResponseDTO(odontologoGuardado)).thenReturn(responseDTO);

        OdontologoResponseDTO resultado = odontologoService.crearOdontologo(requestDTO);

        assertEquals(responseDTO, resultado);
        verify(odontologoMapper).toEntity(requestDTO);
        verify(odontologoRepository).save(odontologoSinGuardar);
        verify(odontologoMapper).toResponseDTO(odontologoGuardado);
    }

    @Test
    void buscarOdontologoPorId_siExiste_devuelveDTO() {
        Long id = 1L;
        Odontologo odontologo = crearOdontologo(id);
        OdontologoResponseDTO responseDTO = crearResponseDTO(id);

        when(odontologoRepository.findById(id)).thenReturn(Optional.of(odontologo));
        when(odontologoMapper.toResponseDTO(odontologo)).thenReturn(responseDTO);

        OdontologoResponseDTO resultado = odontologoService.buscarOdontologoPorId(id);

        assertEquals(responseDTO, resultado);
        verify(odontologoRepository).findById(id);
        verify(odontologoMapper).toResponseDTO(odontologo);
    }

    @Test
    void buscarOdontologoPorId_siNoExiste_lanzaResourceNotFoundException() {
        Long id = 99L;

        when(odontologoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> odontologoService.buscarOdontologoPorId(id));

        verify(odontologoRepository).findById(id);
        verifyNoInteractions(odontologoMapper);
    }

    @Test
    void actualizarOdontologo_siExiste_actualizaYDevuelveDTO() {
        Long id = 1L;
        OdontologoRequestDTO requestDTO = crearRequestDTO();
        Odontologo odontologoExistente = crearOdontologo(id);
        Odontologo odontologoActualizado = crearOdontologo(id);
        OdontologoResponseDTO responseDTO = crearResponseDTO(id);

        when(odontologoRepository.findById(id)).thenReturn(Optional.of(odontologoExistente));
        when(odontologoRepository.save(odontologoExistente)).thenReturn(odontologoActualizado);
        when(odontologoMapper.toResponseDTO(odontologoActualizado)).thenReturn(responseDTO);

        OdontologoResponseDTO resultado = odontologoService.actualizarOdontologo(id, requestDTO);

        assertEquals(responseDTO, resultado);
        verify(odontologoRepository).findById(id);
        verify(odontologoMapper).updateEntityFromDto(requestDTO, odontologoExistente);
        verify(odontologoRepository).save(odontologoExistente);
        verify(odontologoMapper).toResponseDTO(odontologoActualizado);
    }

    @Test
    void eliminarOdontologo_siExiste_elimina() {
        Long id = 1L;
        Odontologo odontologo = crearOdontologo(id);

        when(odontologoRepository.findById(id)).thenReturn(Optional.of(odontologo));

        odontologoService.eliminarOdontologo(id);

        verify(odontologoRepository).findById(id);
        verify(odontologoRepository).delete(odontologo);
    }

    @Test
    void eliminarOdontologo_siNoExiste_lanzaResourceNotFoundException() {
        Long id = 99L;

        when(odontologoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> odontologoService.eliminarOdontologo(id));

        verify(odontologoRepository).findById(id);
        verifyNoInteractions(odontologoMapper);
    }

    private OdontologoRequestDTO crearRequestDTO() {
        return new OdontologoRequestDTO(
                "Ana",
                "Gomez",
                "MAT-123",
                "Ortodoncia"
        );
    }

    private OdontologoResponseDTO crearResponseDTO(Long id) {
        return new OdontologoResponseDTO(
                id,
                "Ana",
                "Gomez",
                "MAT-123",
                "Ortodoncia"
        );
    }

    private Odontologo crearOdontologo(Long id) {
        Odontologo odontologo = new Odontologo();
        odontologo.setId(id);
        odontologo.setNombre("Ana");
        odontologo.setApellido("Gomez");
        odontologo.setMatricula("MAT-123");
        odontologo.setEspecialidad("Ortodoncia");
        return odontologo;
    }
}
