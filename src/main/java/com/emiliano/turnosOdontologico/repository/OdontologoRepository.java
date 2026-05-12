package com.emiliano.turnosOdontologico.repository;

import com.emiliano.turnosOdontologico.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OdontologoRepository extends JpaRepository<Odontologo,Long> {

}
