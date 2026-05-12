package com.emiliano.turnosOdontologico.controller;

import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;
import com.emiliano.turnosOdontologico.service.IOdontologoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    @Autowired
    private IOdontologoService odontologoService;

    @GetMapping("/listarOdontologos")
    public ResponseEntity<List<OdontologoResponseDTO>> listarOdontologos(){
        List<OdontologoResponseDTO> odontologos = odontologoService.listarOdontologos();
        return ResponseEntity.ok(odontologos);
    }

    @PostMapping
    public ResponseEntity<OdontologoResponseDTO> crearOdontologo(@RequestBody @Valid OdontologoRequestDTO dto){
        OdontologoResponseDTO odontologoCreado = odontologoService.crearOdontologo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(odontologoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OdontologoResponseDTO> buscarOdontologoPorId(@PathVariable Long id){
        OdontologoResponseDTO odontologo = odontologoService.buscarOdontologoPorId(id);
        return ResponseEntity.ok(odontologo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OdontologoResponseDTO> actualizarOdontologo(@PathVariable Long id,@RequestBody @Valid OdontologoRequestDTO dto){
        OdontologoResponseDTO odontologoActualizado = odontologoService.actualizarOdontologo(id,dto);
        return ResponseEntity.ok(odontologoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOdontologo(@PathVariable Long id){
        odontologoService.eliminarOdontologo(id);
        return ResponseEntity.noContent().build();
    }
}
