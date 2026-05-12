package com.emiliano.turnosOdontologico.mapper;

import com.emiliano.turnosOdontologico.entity.Odontologo;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoRequestDTO;
import com.emiliano.turnosOdontologico.dto.OdontologoDTO.OdontologoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OdontologoMapper {

    public Odontologo toEntity(OdontologoRequestDTO dto){
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre(dto.nombre());
        odontologo.setApellido(dto.apellido());
        odontologo.setMatricula(dto.matricula());
        odontologo.setEspecialidad(dto.especialidad());
        return odontologo;

    }

    public OdontologoResponseDTO toResponseDTO(Odontologo odontologo) {
        return new OdontologoResponseDTO(
                odontologo.getId(),
                odontologo.getNombre(),
                odontologo.getApellido(),
                odontologo.getMatricula(),
                odontologo.getEspecialidad()
        );
    }

    public void updateEntityFromDto(OdontologoRequestDTO dto, Odontologo odontologo) {
        odontologo.setNombre(dto.nombre());
        odontologo.setApellido(dto.apellido());
        odontologo.setMatricula(dto.matricula());
        odontologo.setEspecialidad(dto.especialidad());
    }


}
