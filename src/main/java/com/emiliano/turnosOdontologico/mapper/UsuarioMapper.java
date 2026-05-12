package com.emiliano.turnosOdontologico.mapper;

import com.emiliano.turnosOdontologico.dto.UsuarioDTO.UsuarioRequestDTO;
import com.emiliano.turnosOdontologico.dto.UsuarioDTO.UsuarioResponseDTO;
import com.emiliano.turnosOdontologico.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol()
        );
    }

    public Usuario toEntity(UsuarioRequestDTO dto, String passwordEncriptada) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.username());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(dto.rol());
        return usuario;
    }

    public void updateEntityFromDTO(Usuario usuario, UsuarioRequestDTO dto, String passwordEncriptada) {
        usuario.setUsername(dto.username());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(dto.rol());
    }
}
