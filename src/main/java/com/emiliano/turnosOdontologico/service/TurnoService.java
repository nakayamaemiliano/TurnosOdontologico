package com.emiliano.turnosOdontologico.service;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.OdontologoMasSolicitadoDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoService implements ITurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private OdontologoRepository odontologoRepository;

    @Autowired
    private TurnoMapper turnoMapper;

    @Override
    public TurnoResponseDTO crearTurno(TurnoRequestDTO dto) {
        if (dto.fecha().isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede registrar un turno en una fecha pasada");
        }
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + dto.pacienteId()));

        Odontologo odontologo = odontologoRepository.findById(dto.odontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + dto.odontologoId()));
        boolean odontologoOcupado = turnoRepository.existsByOdontologoIdAndFechaAndHora(
                dto.odontologoId(),
                dto.fecha(),
                dto.hora()
        );

        if (odontologoOcupado) {
            throw new BusinessException("El odontólogo ya tiene un turno asignado en esa fecha y hora");
        }

        boolean pacienteOcupado = turnoRepository.existsByPacienteIdAndFechaAndHora(
                dto.pacienteId(),
                dto.fecha(),
                dto.hora()
        );

        if (pacienteOcupado) {
            throw new BusinessException("El paciente ya tiene un turno asignado en esa fecha y hora");
        }

        Turno turno = new Turno();
        turno.setFecha(dto.fecha());
        turno.setHora(dto.hora());
        turno.setEstado(EstadoTurno.PENDIENTE);
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);

        Turno turnoGuardado = turnoRepository.save(turno);

        return turnoMapper.toResponseDTO(turnoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> listarTurnos() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoResponseDTO buscarTurnoPorId(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Turno no encontrado con id: " + id));
        return turnoMapper.toResponseDTO(turno);
    }

    @Override
    public TurnoResponseDTO actualizarTurno(Long id, TurnoRequestDTO dto) {
        if (dto.fecha().isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede registrar un turno en una fecha pasada");
        }

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + dto.pacienteId()));

        Odontologo odontologo = odontologoRepository.findById(dto.odontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + dto.odontologoId()));

        boolean cambioOdontologoFechaOHora =
                !turno.getOdontologo().getId().equals(dto.odontologoId())
                        || !turno.getFecha().equals(dto.fecha())
                        || !turno.getHora().equals(dto.hora());

        if (cambioOdontologoFechaOHora) {
            boolean odontologoOcupado = turnoRepository.existsByOdontologoIdAndFechaAndHora(
                    dto.odontologoId(),
                    dto.fecha(),
                    dto.hora()
            );

            if (odontologoOcupado) {
                throw new BusinessException("El odontólogo ya tiene un turno asignado en esa fecha y hora");
            }
        }

        boolean cambioPacienteFechaOHora =
                !turno.getPaciente().getId().equals(dto.pacienteId())
                        || !turno.getFecha().equals(dto.fecha())
                        || !turno.getHora().equals(dto.hora());

        if (cambioPacienteFechaOHora) {
            boolean pacienteOcupado = turnoRepository.existsByPacienteIdAndFechaAndHora(
                    dto.pacienteId(),
                    dto.fecha(),
                    dto.hora()
            );

            if (pacienteOcupado) {
                throw new BusinessException("El paciente ya tiene un turno asignado en esa fecha y hora");
            }
        }

        turno.setFecha(dto.fecha());
        turno.setHora(dto.hora());
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);

        Turno turnoActualizado = turnoRepository.save(turno);

        return turnoMapper.toResponseDTO(turnoActualizado);
    }

    @Override
    public void eliminarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        turnoRepository.delete(turno);

    }

    @Override
    public List<TurnoResponseDTO> obtenerTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findByFecha(fecha)
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> obtenerTurnosPorOdontologo(Long idOdontologo) {
        if (!odontologoRepository.existsById(idOdontologo)) {
            throw new ResourceNotFoundException("Odontólogo no encontrado con id: " + idOdontologo);
        }

        return turnoRepository.findByOdontologoId(idOdontologo)
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> obtenerTurnosPorPaciente(Long idPaciente) {
        if (!pacienteRepository.existsById(idPaciente)) {
            throw new ResourceNotFoundException("Paciente no encontrado con id: " + idPaciente);
        }

        return turnoRepository.findByPacienteId(idPaciente)
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public TurnoResponseDTO confirmarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        if (turno.getEstado() != EstadoTurno.PENDIENTE) {
            throw new BusinessException("Solo se pueden confirmar turnos en estado PENDIENTE");
        }

        turno.setEstado(EstadoTurno.CONFIRMADO);

        Turno turnoConfirmado = turnoRepository.save(turno);

        return turnoMapper.toResponseDTO(turnoConfirmado);
    }

    @Override
    public TurnoResponseDTO cancelarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        if (turno.getEstado() == EstadoTurno.ATENDIDO) {
            throw new BusinessException("No se puede cancelar un turno que ya fue marcado como ATENDIDO");
        }

        turno.setEstado(EstadoTurno.CANCELADO);

        Turno turnoCancelado = turnoRepository.save(turno);

        return turnoMapper.toResponseDTO(turnoCancelado);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenTurnosPorEstadoDTO obtenerResumenPorFecha(LocalDate fecha) {
        long pendientes = turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.PENDIENTE);
        long confirmados = turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.CONFIRMADO);
        long cancelados = turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.CANCELADO);
        long atendidos = turnoRepository.countByFechaAndEstado(fecha, EstadoTurno.ATENDIDO);

        return new ResumenTurnosPorEstadoDTO(
                fecha,
                pendientes,
                confirmados,
                cancelados,
                atendidos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OdontologoMasSolicitadoDTO obtenerOdontologoMasSolicitado() {
        List<Turno> turnosAtendidos = turnoRepository.findByEstado(EstadoTurno.ATENDIDO);

        if (turnosAtendidos.isEmpty()) {
            throw new ResourceNotFoundException("No existen turnos atendidos para calcular el odontólogo más solicitado");
        }

        Map<Odontologo, Long> cantidadPorOdontologo = turnosAtendidos.stream()
                .collect(Collectors.groupingBy(
                        Turno::getOdontologo,
                        Collectors.counting()
                ));

        Map.Entry<Odontologo, Long> odontologoMasSolicitado = cantidadPorOdontologo.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existen turnos atendidos para calcular el odontólogo más solicitado"
                ));

        Odontologo odontologo = odontologoMasSolicitado.getKey();
        Long cantidad = odontologoMasSolicitado.getValue();

        return new OdontologoMasSolicitadoDTO(
                odontologo.getId(),
                odontologo.getNombre(),
                odontologo.getApellido(),
                odontologo.getEspecialidad(),
                cantidad
        );
    }

}
