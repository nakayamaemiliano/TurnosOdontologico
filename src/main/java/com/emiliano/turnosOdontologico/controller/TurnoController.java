package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.OdontologoMasSolicitadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.ResumenTurnosPorEstadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoRequestDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoResponseDTO;
import com.emiliano.turnosOdontologico.service.ITurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Turnos", description = "Operaciones para gestionar turnos odontologicos")
@RestController
@RequestMapping("/turnos")
public class TurnoController {
    @Autowired
    private ITurnoService turnoService;

    @Operation(summary = "Crear turno", description = "Registra un nuevo turno para un paciente y un odontologo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear turnos"),
            @ApiResponse(responseCode = "404", description = "Paciente u odontologo no encontrado")
    })
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@RequestBody @Valid TurnoRequestDTO dto) {
        TurnoResponseDTO turnoCreado = turnoService.crearTurno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoCreado);
    }

    @Operation(summary = "Listar turnos", description = "Devuelve todos los turnos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnos() {
        List<TurnoResponseDTO> turnos = turnoService.listarTurnos();
        return ResponseEntity.ok(turnos);
    }

    @Operation(summary = "Buscar turno por ID", description = "Devuelve un turno segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> buscarTurnoPorId(@PathVariable Long id) {
        TurnoResponseDTO turno = turnoService.buscarTurnoPorId(id);
        return ResponseEntity.ok(turno);
    }

    @Operation(summary = "Actualizar turno", description = "Actualiza fecha, hora, paciente u odontologo de un turno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar turnos"),
            @ApiResponse(responseCode = "404", description = "Turno, paciente u odontologo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> actualizarTurno(
            @PathVariable Long id,
            @RequestBody @Valid TurnoRequestDTO dto) {

        TurnoResponseDTO turnoActualizado = turnoService.actualizarTurno(id, dto);
        return ResponseEntity.ok(turnoActualizado);
    }

    @Operation(summary = "Eliminar turno", description = "Elimina un turno existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Turno eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar turnos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar turnos por fecha", description = "Devuelve los turnos registrados en una fecha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos encontrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorFecha(fecha);
        return ResponseEntity.ok(turnos);
    }

    @Operation(summary = "Buscar turnos por odontologo", description = "Devuelve los turnos asociados a un odontologo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos encontrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado")
    })
    @GetMapping("/odontologo/{idOdontologo}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorOdontologo(@PathVariable Long idOdontologo) {
        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorOdontologo(idOdontologo);
        return ResponseEntity.ok(turnos);
    }

    @Operation(summary = "Buscar turnos por paciente", description = "Devuelve los turnos asociados a un paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turnos encontrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorPaciente(@PathVariable Long idPaciente) {
        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorPaciente(idPaciente);
        return ResponseEntity.ok(turnos);
    }

    @Operation(summary = "Confirmar turno", description = "Cambia un turno pendiente a confirmado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno confirmado correctamente"),
            @ApiResponse(responseCode = "400", description = "El turno no puede confirmarse en su estado actual"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para confirmar turnos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<TurnoResponseDTO> confirmarTurno(@PathVariable Long id) {
        TurnoResponseDTO turnoConfirmado = turnoService.confirmarTurno(id);
        return ResponseEntity.ok(turnoConfirmado);
    }

    @Operation(summary = "Cancelar turno", description = "Cambia un turno a estado cancelado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno cancelado correctamente"),
            @ApiResponse(responseCode = "400", description = "El turno no puede cancelarse en su estado actual"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para cancelar turnos"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TurnoResponseDTO> cancelarTurno(@PathVariable Long id) {
        TurnoResponseDTO turnoCancelado = turnoService.cancelarTurno(id);
        return ResponseEntity.ok(turnoCancelado);
    }

    @Operation(summary = "Resumen de turnos por fecha", description = "Devuelve la cantidad de turnos por estado para una fecha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/resumen/{fecha}")
    public ResponseEntity<ResumenTurnosPorEstadoDTO> obtenerResumenPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        ResumenTurnosPorEstadoDTO resumen = turnoService.obtenerResumenPorFecha(fecha);
        return ResponseEntity.ok(resumen);
    }

    @Operation(summary = "Odontologo mas solicitado", description = "Devuelve el odontologo con mayor cantidad de turnos atendidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No existen turnos atendidos")
    })
    @GetMapping("/odontologo-mas-solicitado")
    public ResponseEntity<OdontologoMasSolicitadoDTO> obtenerOdontologoMasSolicitado() {
        OdontologoMasSolicitadoDTO odontologo = turnoService.obtenerOdontologoMasSolicitado();
        return ResponseEntity.ok(odontologo);
    }

}
