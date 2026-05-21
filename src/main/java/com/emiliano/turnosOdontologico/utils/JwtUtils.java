package com.emiliano.turnosOdontologico.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    //creacion de tokens
    public String createToken(Authentication authentication){

        Algorithm algorithm = Algorithm.HMAC256(privateKey);


        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities" , authorities)
                .withIssuedAt(new Date())
                // vence en 30 min
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000 ))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1800000 ))
                .sign(algorithm);


        return jwtToken;
    }

    //decodifica y valida los tokens que creo
    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            //si todo esta ok, no genera ninguna exception y devuelve jwt ya decodificado
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;

        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token invalido. No autorizado");
        }
    }

    //obtener usuario
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    //obtener un solo claim en particular
    public Claim getSpecificClaim(DecodedJWT decodedJWT,String clainName){
        return decodedJWT.getClaim(clainName);
    }


    //obtener todos los claims
    public Map<String,Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

}
