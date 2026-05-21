package com.emiliano.turnosOdontologico.security.controller;

import com.emiliano.turnosOdontologico.dto.AuthLoginRequestDTO;
import com.emiliano.turnosOdontologico.dto.AuthResponseDTO;
import com.emiliano.turnosOdontologico.security.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody  @Valid AuthLoginRequestDTO userRequest){
        return new ResponseEntity<>(this.userDetailsServiceImp.loginUser(userRequest),HttpStatus.OK);
    }

}
