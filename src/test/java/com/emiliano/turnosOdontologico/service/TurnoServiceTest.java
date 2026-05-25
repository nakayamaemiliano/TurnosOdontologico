package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.ResumenTurnosPorEstadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoRequestDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoResponseDTO;
import com.emiliano.turnosOdontologico.entity.Odontologo;
import com.emiliano.turnosOdontologico.entity.Paciente;
import com.emiliano.turnosOdontologico.entity.Turno;
import com.emiliano.turnosOdontologico.enums.EstadoTurno;
import com.emiliano.turnosOdontologico.exception.BusinessException;
import com.emiliano.turnosOdontologico.exception.ResourceNotFoundException;
import com.emiliano.turnosOdontologico.mapper.TurnoMapper;
import com.emiliano.turnosOdontologico.repository.OdontologoRepository;
import com.emiliano.turnosOdontologico.repository.PacienteRepository;
import com.emiliano.turnosOdontologico.repository.TurnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private TurnoMapper turnoMapper;

    @InjectMocks
    private TurnoService turnoService;

    @Test
    void crearTurno_conFechaPasada_lanzaBusinessException() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().minusDays(1));

        assertThrows(BusinessException.class,
                () -> turnoService.crearTurno(requestDTO));

        verifyNoInteractions(pacienteRepository);
        verifyNoInteractions(odontologoRepository);
        verifyNoInteractions(turnoRepository);
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void crearTurno_siPacienteNoExiste_lanzaResourceNotFoundException() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().plusDays(1));

        when(pacienteRepository.findById(requestDTO.pacienteId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> turnoService.crearTurno(requestDTO));

        verify(pacienteRepository).findById(requestDTO.pacienteId());
        verifyNoInteractions(odontologoRepository);
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void crearTurno_siOdontologoNoExiste_lanzaResourceNotFoundException() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().plusDays(1));
        Paciente paciente = crearPaciente(1L);

        when(pacienteRepository.findById(requestDTO.pacienteId()))
                .thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(requestDTO.odontologoId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> turnoService.crearTurno(requestDTO));

        verify(pacienteRepository).findById(requestDTO.pacienteId());
        verify(odontologoRepository).findById(requestDTO.odontologoId());
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void crearTurno_siOdontologoOcupado_lanzaBusinessException() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().plusDays(1));
        Paciente paciente = crearPaciente(1L);
        Odontologo odontologo = crearOdontologo(1L);

        when(pacienteRepository.findById(requestDTO.pacienteId()))
                .thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(requestDTO.odontologoId()))
                .thenReturn(Optional.of(odontologo));
        when(turnoRepository.existsByOdontologoIdAndFechaAndHora(
                requestDTO.odontologoId(),
                requestDTO.fecha(),
                requestDTO.hora()
        )).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> turnoService.crearTurno(requestDTO));

        verify(turnoRepository, never()).save(any(Turno.class));
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void crearTurno_siPacienteOcupado_lanzaBusinessException() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().plusDays(1));
        Paciente paciente = crearPaciente(1L);
        Odontologo odontologo = crearOdontologo(1L);

        when(pacienteRepository.findById(requestDTO.pacienteId()))
                .thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(requestDTO.odontologoId()))
                .thenReturn(Optional.of(odontologo));
        when(turnoRepository.existsByOdontologoIdAndFechaAndHora(
                requestDTO.odontologoId(),
                requestDTO.fecha(),
                requestDTO.hora()
        )).thenReturn(false);
        when(turnoRepository.existsByPacienteIdAndFechaAndHora(
                requestDTO.pacienteId(),
                requestDTO.fecha(),
                requestDTO.hora()
        )).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> turnoService.crearTurno(requestDTO));

        verify(turnoRepository, never()).save(any(Turno.class));
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void crearTurno_valido_guardaTurnoPendiente() {
        TurnoRequestDTO requestDTO = crearRequestDTO(LocalDate.now().plusDays(1));
        Paciente paciente = crearPaciente(1L);
        Odontologo odontologo = crearOdontologo(1L);
        TurnoResponseDTO responseDTO = crearResponseDTO(1L, EstadoTurno.PENDIENTE);

        when(pacienteRepository.findById(requestDTO.pacienteId()))
                .thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(requestDTO.odontologoId()))
                .thenReturn(Optional.of(odontologo));
        when(turnoRepository.existsByOdontologoIdAndFechaAndHora(
                requestDTO.odontologoId(),
                requestDTO.fecha(),
                requestDTO.hora()
        )).thenReturn(false);
        when(turnoRepository.existsByPacienteIdAndFechaAndHora(
                requestDTO.pacienteId(),
                requestDTO.fecha(),
                requestDTO.hora()
        )).thenReturn(false);
        when(turnoRepository.save(any(Turno.class))).thenAnswer(invocation -> {
            Turno turno = invocation.getArgument(0);
            turno.setId(1L);
            return turno;
        });
        when(turnoMapper.toResponseDTO(any(Turno.class))).thenReturn(responseDTO);

        TurnoResponseDTO resultado = turnoService.crearTurno(requestDTO);

        assertEquals(responseDTO, resultado);

        ArgumentCaptor<Turno> turnoCaptor = ArgumentCaptor.forClass(Turno.class);
        verify(turnoRepository).save(turnoCaptor.capture());

        Turno turnoGuardado = turnoCaptor.getValue();
        assertEquals(EstadoTurno.PENDIENTE, turnoGuardado.getEstado());
        assertEquals(paciente, turnoGuardado.getPaciente());
        assertEquals(odontologo, turnoGuardado.getOdontologo());
    }

    @Test
    void confirmarTurno_siEstadoPendiente_cambiaAConfirmado() {
        Long id = 1L;
        Turno turno = crearTurno(id, EstadoTurno.PENDIENTE);
        TurnoResponseDTO responseDTO = crearResponseDTO(id, EstadoTurno.CONFIRMADO);

        when(turnoRepository.findById(id)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);
        when(turnoMapper.toResponseDTO(turno)).thenReturn(responseDTO);

        TurnoResponseDTO resultado = turnoService.confirmarTurno(id);

        assertEquals(responseDTO, resultado);
        assertEquals(EstadoTurno.CONFIRMADO, turno.getEstado());
        verify(turnoRepository).save(turno);
        verify(turnoMapper).toResponseDTO(turno);
    }

    @Test
    void confirmarTurno_siNoPendiente_lanzaBusinessException() {
        Long id = 1L;
        Turno turno = crearTurno(id, EstadoTurno.CONFIRMADO);

        when(turnoRepository.findById(id)).thenReturn(Optional.of(turno));

        assertThrows(BusinessException.class,
                () -> turnoService.confirmarTurno(id));

        verify(turnoRepository).findById(id);
        verify(turnoRepository, never()).save(any(Turno.class));
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void cancelarTurno_siAtendido_lanzaBusinessException() {
        Long id = 1L;
        Turno turno = crearTurno(id, EstadoTurno.ATENDIDO);

        when(turnoRepository.findById(id)).thenReturn(Optional.of(turno));

        assertThrows(BusinessException.class,
                () -> turnoService.cancelarTurno(id));

        verify(turnoRepository).findById(id);
        verify(turnoRepository, never()).save(any(Turno.class));
        verifyNoInteractions(turnoMapper);
    }

    @Test
    void obtenerResumenPorFecha_devuelveConteos() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        when(turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.PENDIENTE)).thenReturn(2L);
        when(turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.CONFIRMADO)).thenReturn(3L);
        when(turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.CANCELADO)).thenReturn(1L);
        when(turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.ATENDIDO)).thenReturn(4L);

        ResumenTurnosPorEstadoDTO resultado = turnoService.obtenerResumenPorFecha(fecha);

        assertEquals(fecha, resultado.fecha());
        assertEquals(2L, resultado.cantidadPendientes());
        assertEquals(3L, resultado.cantidadConfirmados());
        assertEquals(1L, resultado.cantidadCancelados());
        assertEquals(4L, resultado.cantidadAtendidos());
    }

    private TurnoRequestDTO crearRequestDTO(LocalDate fecha) {
        return new TurnoRequestDTO(
                fecha,
                LocalTime.of(10, 0),
                1L,
                1L
        );
    }

    private TurnoResponseDTO crearResponseDTO(Long id, EstadoTurno estado) {
        return new TurnoResponseDTO(
                id,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                estado,
                1L,
                "Juan",
                "Perez",
                1L,
                "Ana",
                "Gomez",
                "Ortodoncia"
        );
    }

    private Turno crearTurno(Long id, EstadoTurno estado) {
        Turno turno = new Turno();
        turno.setId(id);
        turno.setFecha(LocalDate.now().plusDays(1));
        turno.setHora(LocalTime.of(10, 0));
        turno.setEstado(estado);
        turno.setPaciente(crearPaciente(1L));
        turno.setOdontologo(crearOdontologo(1L));
        return turno;
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
