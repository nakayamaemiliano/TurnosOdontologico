package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;
import com.emiliano.turnosOdontologico.entity.Paciente;
import com.emiliano.turnosOdontologico.service.IPacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private IPacienteService pacienteService;

    @GetMapping("/listarPacientes")
    public ResponseEntity<List<PacienteResponseDTO>> listarPacientes(){
        List<PacienteResponseDTO>pacientes = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacientes);
    }
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> crearPaciente(@RequestBody @Valid PacienteRequestDTO dto){
        PacienteResponseDTO pacienteCreado = pacienteService.crearPaciente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteCreado);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(@PathVariable Long id){
        PacienteResponseDTO paciente = pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(paciente);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(@PathVariable Long id, @RequestBody @Valid PacienteRequestDTO dto){
        PacienteResponseDTO pacienteActualizado = pacienteService.actualizarPaciente(id,dto);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id){
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}
