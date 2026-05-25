package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;
import com.emiliano.turnosOdontologico.entity.Paciente;
import com.emiliano.turnosOdontologico.exception.ResourceNotFoundException;
import com.emiliano.turnosOdontologico.mapper.PacienteMapper;
import com.emiliano.turnosOdontologico.repository.PacienteRepository;
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
public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void listarPacientes_devuelveLista() {
        Paciente paciente = crearPaciente(1L);
        PacienteResponseDTO responseDTO = crearResponseDTO(1L);

        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));
        when(pacienteMapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        List<PacienteResponseDTO> resultado = pacienteService.listarPacientes();

        assertEquals(1, resultado.size());
        assertEquals(responseDTO, resultado.get(0));
        verify(pacienteRepository).findAll();
        verify(pacienteMapper).toResponseDTO(paciente);
    }
    @Test
    void crearPaciente_guardaYDevuelveDTO() {
        PacienteRequestDTO requestDTO = crearRequestDTO();
        Paciente pacienteSinGuardar = crearPaciente(null);
        Paciente pacienteGuardado = crearPaciente(1L);
        PacienteResponseDTO responseDTO = crearResponseDTO(1L);

        when(pacienteMapper.toEntity(requestDTO)).thenReturn(pacienteSinGuardar);
        when(pacienteRepository.save(pacienteSinGuardar)).thenReturn(pacienteGuardado);
        when(pacienteMapper.toResponseDTO(pacienteGuardado)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = pacienteService.crearPaciente(requestDTO);

        assertEquals(responseDTO, resultado);
        verify(pacienteMapper).toEntity(requestDTO);
        verify(pacienteRepository).save(pacienteSinGuardar);
        verify(pacienteMapper).toResponseDTO(pacienteGuardado);
    }

    @Test
    void buscarPacientePorId_siExiste_devuelveDTO() {
        Long id = 1L;
        Paciente paciente = crearPaciente(id);
        PacienteResponseDTO responseDTO = crearResponseDTO(id);

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(pacienteMapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = pacienteService.buscarPacientePorId(id);

        assertEquals(responseDTO, resultado);
        verify(pacienteRepository).findById(id);
        verify(pacienteMapper).toResponseDTO(paciente);
    }

    @Test
    void buscarPacientePorId_siNoExiste_lanzaResourceNotFoundException() {
        Long id = 99L;

        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pacienteService.buscarPacientePorId(id));

        verify(pacienteRepository).findById(id);
        verifyNoInteractions(pacienteMapper);
    }

    @Test
    void actualizarPaciente_siExiste_actualizaYDevuelveDTO() {
        Long id = 1L;
        PacienteRequestDTO requestDTO = crearRequestDTO();
        Paciente pacienteExistente = crearPaciente(id);
        Paciente pacienteActualizado = crearPaciente(id);
        PacienteResponseDTO responseDTO = crearResponseDTO(id);

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteActualizado);
        when(pacienteMapper.toResponseDTO(pacienteActualizado)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = pacienteService.actualizarPaciente(id, requestDTO);

        assertEquals(responseDTO, resultado);
        verify(pacienteRepository).findById(id);
        verify(pacienteMapper).updateEntityFromDTO(pacienteExistente, requestDTO);
        verify(pacienteRepository).save(pacienteExistente);
        verify(pacienteMapper).toResponseDTO(pacienteActualizado);
    }

    @Test
    void eliminarPaciente_siExiste_elimina() {
        Long id = 1L;
        Paciente paciente = crearPaciente(id);

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));

        pacienteService.eliminarPaciente(id);

        verify(pacienteRepository).findById(id);
        verify(pacienteRepository).delete(paciente);
    }

    @Test
    void eliminarPaciente_siNoExiste_lanzaResourceNotFoundException() {
        Long id = 99L;

        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pacienteService.eliminarPaciente(id));

        verify(pacienteRepository).findById(id);
        verifyNoInteractions(pacienteMapper);
    }

    private PacienteRequestDTO crearRequestDTO() {
        return new PacienteRequestDTO(
                "Juan",
                "Perez",
                "12345678",
                "juan.perez@mail.com",
                "1122334455"
        );
    }

    private PacienteResponseDTO crearResponseDTO(Long id) {
        return new PacienteResponseDTO(
                id,
                "Juan",
                "Perez",
                "12345678",
                "juan.perez@mail.com",
                "1122334455"
        );
    }

    private Paciente crearPaciente(Long id) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");
        paciente.setDni("12345678");
        paciente.setEmail("juan.perez@mail.com");
        paciente.setTelefono("1122334455");
        return paciente;
    }


}
