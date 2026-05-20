package com.emiliano.turnosOdontologico.repository;

import com.emiliano.turnosOdontologico.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OdontologoRepository extends JpaRepository<Odontologo,Long> {

}
