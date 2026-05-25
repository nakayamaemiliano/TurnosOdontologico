package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteRequestDTO;
import com.emiliano.turnosOdontologico.dto.PacienteDTO.PacienteResponseDTO;
import com.emiliano.turnosOdontologico.service.IPacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pacientes", description = "Operaciones para gestionar pacientes")
@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private IPacienteService pacienteService;

    @Operation(summary = "Listar pacientes", description = "Devuelve todos los pacientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/listarPacientes")
    public ResponseEntity<List<PacienteResponseDTO>> listarPacientes(){
        List<PacienteResponseDTO>pacientes = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Crear paciente", description = "Registra un nuevo paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear pacientes")
    })
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> crearPaciente(@RequestBody @Valid PacienteRequestDTO dto){
        PacienteResponseDTO pacienteCreado = pacienteService.crearPaciente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteCreado);
    }

    @Operation(summary = "Buscar paciente por ID", description = "Devuelve un paciente segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(@PathVariable Long id){
        PacienteResponseDTO paciente = pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(paciente);
    }

    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar pacientes"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(@PathVariable Long id, @RequestBody @Valid PacienteRequestDTO dto){
        PacienteResponseDTO pacienteActualizado = pacienteService.actualizarPaciente(id,dto);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar pacientes"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id){
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}
