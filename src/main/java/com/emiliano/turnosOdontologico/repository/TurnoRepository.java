package com.emiliano.turnosOdontologico.repository;

import com.emiliano.turnosOdontologico.entity.Turno;
import com.emiliano.turnosOdontologico.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno,Long> {



}
