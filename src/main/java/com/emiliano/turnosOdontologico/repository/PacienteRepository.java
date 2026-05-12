package com.emiliano.turnosOdontologico.repository;

import com.emiliano.turnosOdontologico.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long> {




}
