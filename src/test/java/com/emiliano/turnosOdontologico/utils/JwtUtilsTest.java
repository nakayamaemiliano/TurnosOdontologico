package com.emiliano.turnosOdontologico.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(
                jwtUtils,
                "privateKey",
                "clave-secreta-larga-para-testing-123456"
        );

        ReflectionTestUtils.setField(
                jwtUtils,
                "userGenerator",
                "TEST_GENERATOR"
        );
    }

    @Test
    void createToken_generaTokenConUsernameYAuthorities() {
        Authentication authentication = crearAuthentication();

        String token = jwtUtils.createToken(authentication);

        assertFalse(token.isBlank());

        DecodedJWT decodedJWT = jwtUtils.validateToken(token);

        assertEquals("admin", decodedJWT.getSubject());
        assertEquals("TEST_GENERATOR", decodedJWT.getIssuer());
        assertEquals("ROLE_ADMIN,READ", decodedJWT.getClaim("authorities").asString());
    }

    @Test
    void validateToken_conTokenValido_devuelveDecodedJWT() {
        Authentication authentication = crearAuthentication();
        String token = jwtUtils.createToken(authentication);

        DecodedJWT decodedJWT = jwtUtils.validateToken(token);

        assertEquals("admin", jwtUtils.extractUsername(decodedJWT));
    }

    @Test
    void validateToken_conTokenInvalido_lanzaJWTVerificationException() {
        String tokenInvalido = "token.invalido.fake";

        assertThrows(JWTVerificationException.class,
                () -> jwtUtils.validateToken(tokenInvalido));
    }

    @Test
    void extractUsername_devuelveSubject() {
        Authentication authentication = crearAuthentication();
        String token = jwtUtils.createToken(authentication);
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);

        String username = jwtUtils.extractUsername(decodedJWT);

        assertEquals("admin", username);
    }

    @Test
    void getSpecificClaim_devuelveClaimSolicitado() {
        Authentication authentication = crearAuthentication();
        String token = jwtUtils.createToken(authentication);
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);

        String authorities = jwtUtils
                .getSpecificClaim(decodedJWT, "authorities")
                .asString();

        assertEquals("ROLE_ADMIN,READ", authorities);
    }

    private Authentication crearAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                "admin",
                "password",
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("READ")
                )
        );
    }
}
