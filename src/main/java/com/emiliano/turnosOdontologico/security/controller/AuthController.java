package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.dto.AuthLoginRequestDTO;
import com.emiliano.turnosOdontologico.dto.AuthResponseDTO;
import com.emiliano.turnosOdontologico.security.service.UserDetailsServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticacion", description = "Login y generacion de token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Operation(summary = "Iniciar sesion", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody  @Valid AuthLoginRequestDTO userRequest){
        return new ResponseEntity<>(this.userDetailsServiceImp.loginUser(userRequest),HttpStatus.OK);
    }

}
