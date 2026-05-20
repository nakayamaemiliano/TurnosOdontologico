package com.emiliano.turnosOdontologico.repository;

import com.emiliano.turnosOdontologico.entity.Turno;
import com.emiliano.turnosOdontologico.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Repository
public interface TurnoRepository extends JpaRepository<Turno,Long> {

    List<Turno> findByFecha(LocalDate fecha);

    List<Turno> findByOdontologoId(Long odontologoId);

    List<Turno> findByPacienteId(Long pacienteId);

    boolean existsByOdontologoIdAndFechaAndHora(Long odontologoId, LocalDate fecha, LocalTime hora);

    boolean existsByPacienteIdAndFechaAndHora(Long pacienteId, LocalDate fecha, LocalTime hora);

    long countByFechaAndEstado(LocalDate fecha, EstadoTurno estado);

    List<Turno> findByEstado(EstadoTurno estado);
}
