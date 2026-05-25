package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;
import com.emiliano.turnosOdontologico.service.IOdontologoService;
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

@Tag(name = "Odontologos", description = "Operaciones para gestionar odontologos")
@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    @Autowired
    private IOdontologoService odontologoService;

    @Operation(summary = "Listar odontologos", description = "Devuelve todos los odontologos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/listarOdontologos")
    public ResponseEntity<List<OdontologoResponseDTO>> listarOdontologos(){
        List<OdontologoResponseDTO> odontologos = odontologoService.listarOdontologos();
        return ResponseEntity.ok(odontologos);
    }

    @Operation(summary = "Crear odontologo", description = "Registra un nuevo odontologo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Odontologo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear odontologos")
    })
    @PostMapping
    public ResponseEntity<OdontologoResponseDTO> crearOdontologo(@RequestBody @Valid OdontologoRequestDTO dto){
        OdontologoResponseDTO odontologoCreado = odontologoService.crearOdontologo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(odontologoCreado);
    }

    @Operation(summary = "Buscar odontologo por ID", description = "Devuelve un odontologo segun su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoResponseDTO> buscarOdontologoPorId(@PathVariable Long id){
        OdontologoResponseDTO odontologo = odontologoService.buscarOdontologoPorId(id);
        return ResponseEntity.ok(odontologo);
    }

    @Operation(summary = "Actualizar odontologo", description = "Actualiza los datos de un odontologo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar odontologos"),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OdontologoResponseDTO> actualizarOdontologo(@PathVariable Long id,@RequestBody @Valid OdontologoRequestDTO dto){
        OdontologoResponseDTO odontologoActualizado = odontologoService.actualizarOdontologo(id,dto);
        return ResponseEntity.ok(odontologoActualizado);
    }

    @Operation(summary = "Eliminar odontologo", description = "Elimina un odontologo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Odontologo eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar odontologos"),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOdontologo(@PathVariable Long id){
        odontologoService.eliminarOdontologo(id);
        return ResponseEntity.noContent().build();
    }
}
