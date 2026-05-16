package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.TurnoDTO.OdontologoMasSolicitadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.ResumenTurnosPorEstadoDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoRequestDTO;
import com.emiliano.turnosOdontologico.dto.TurnoDTO.TurnoResponseDTO;
import com.emiliano.turnosOdontologico.service.ITurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    @Autowired
    private ITurnoService turnoService;

    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@RequestBody @Valid TurnoRequestDTO dto) {
        TurnoResponseDTO turnoCreado = turnoService.crearTurno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoCreado);
    }

    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnos() {
        List<TurnoResponseDTO> turnos = turnoService.listarTurnos();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> buscarTurnoPorId(@PathVariable Long id) {
        TurnoResponseDTO turno = turnoService.buscarTurnoPorId(id);
        return ResponseEntity.ok(turno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> actualizarTurno(
            @PathVariable Long id,
            @RequestBody @Valid TurnoRequestDTO dto) {

        TurnoResponseDTO turnoActualizado = turnoService.actualizarTurno(id, dto);
        return ResponseEntity.ok(turnoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorFecha(fecha);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/odontologo/{idOdontologo}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorOdontologo(@PathVariable Long idOdontologo) {
        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorOdontologo(idOdontologo);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarTurnosPorPaciente(@PathVariable Long idPaciente) {
        List<TurnoResponseDTO> turnos = turnoService.obtenerTurnosPorPaciente(idPaciente);
        return ResponseEntity.ok(turnos);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<TurnoResponseDTO> confirmarTurno(@PathVariable Long id) {
        TurnoResponseDTO turnoConfirmado = turnoService.confirmarTurno(id);
        return ResponseEntity.ok(turnoConfirmado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TurnoResponseDTO> cancelarTurno(@PathVariable Long id) {
        TurnoResponseDTO turnoCancelado = turnoService.cancelarTurno(id);
        return ResponseEntity.ok(turnoCancelado);
    }

    @GetMapping("/resumen/{fecha}")
    public ResponseEntity<ResumenTurnosPorEstadoDTO> obtenerResumenPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        ResumenTurnosPorEstadoDTO resumen = turnoService.obtenerResumenPorFecha(fecha);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/odontologo-mas-solicitado")
    public ResponseEntity<OdontologoMasSolicitadoDTO> obtenerOdontologoMasSolicitado() {
        OdontologoMasSolicitadoDTO odontologo = turnoService.obtenerOdontologoMasSolicitado();
        return ResponseEntity.ok(odontologo);
    }

}
