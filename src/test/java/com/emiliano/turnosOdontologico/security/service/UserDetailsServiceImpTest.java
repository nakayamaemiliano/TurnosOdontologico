package com.emiliano.turnosOdontologico.security.service;

import com.emiliano.turnosOdontologico.dto.AuthLoginRequestDTO;
import com.emiliano.turnosOdontologico.dto.AuthResponseDTO;
import com.emiliano.turnosOdontologico.security.model.Permission;
import com.emiliano.turnosOdontologico.security.model.Role;
import com.emiliano.turnosOdontologico.security.model.UserSec;
import com.emiliano.turnosOdontologico.security.repository.UserSecRepository;
import com.emiliano.turnosOdontologico.utils.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImpTest {
    @Mock
    private UserSecRepository userRepo;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImp userDetailsService;

    @AfterEach
    void limpiarSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loadUserByUsername_siExiste_devuelveUserDetailsConRolesYPermisos() {
        String username = "admin";
        UserSec userSec = crearUsuarioAdmin();

        when(userRepo.findUserEntityByUsername(username))
                .thenReturn(Optional.of(userSec));

        UserDetails resultado = userDetailsService.loadUserByUsername(username);

        assertEquals(username, resultado.getUsername());
        assertEquals("password-encriptada", resultado.getPassword());
        assertTrue(resultado.isEnabled());
        assertTrue(resultado.isAccountNonExpired());
        assertTrue(resultado.isAccountNonLocked());
        assertTrue(resultado.isCredentialsNonExpired());

        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));

        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("READ")));

        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("CREATE")));

        verify(userRepo).findUserEntityByUsername(username);
    }

    @Test
    void loadUserByUsername_siNoExiste_lanzaUsernameNotFoundException() {
        String username = "no_existe";

        when(userRepo.findUserEntityByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));

        verify(userRepo).findUserEntityByUsername(username);
    }

    @Test
    void authenticate_conPasswordCorrecta_devuelveAuthentication() {
        String username = "admin";
        String rawPassword = "admin123";
        UserSec userSec = crearUsuarioAdmin();

        when(userRepo.findUserEntityByUsername(username))
                .thenReturn(Optional.of(userSec));
        when(passwordEncoder.matches(rawPassword, userSec.getPassword()))
                .thenReturn(true);

        Authentication resultado = userDetailsService.authenticate(username, rawPassword);

        assertNotNull(resultado);
        assertTrue(resultado.isAuthenticated());
        assertEquals(username, resultado.getPrincipal());
        assertEquals(userSec.getPassword(), resultado.getCredentials());

        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepo).findUserEntityByUsername(username);
        verify(passwordEncoder).matches(rawPassword, userSec.getPassword());
    }

    @Test
    void authenticate_conPasswordIncorrecta_lanzaBadCredentialsException() {
        String username = "admin";
        String rawPassword = "password-mal";
        UserSec userSec = crearUsuarioAdmin();

        when(userRepo.findUserEntityByUsername(username))
                .thenReturn(Optional.of(userSec));
        when(passwordEncoder.matches(rawPassword, userSec.getPassword()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> userDetailsService.authenticate(username, rawPassword));

        verify(userRepo).findUserEntityByUsername(username);
        verify(passwordEncoder).matches(rawPassword, userSec.getPassword());
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void loginUser_conCredencialesValidas_devuelveJwt() {
        String username = "admin";
        String rawPassword = "admin123";
        String token = "jwt-token";
        UserSec userSec = crearUsuarioAdmin();
        AuthLoginRequestDTO requestDTO = new AuthLoginRequestDTO(username, rawPassword);

        when(userRepo.findUserEntityByUsername(username))
                .thenReturn(Optional.of(userSec));
        when(passwordEncoder.matches(rawPassword, userSec.getPassword()))
                .thenReturn(true);
        when(jwtUtils.createToken(org.mockito.ArgumentMatchers.any(Authentication.class)))
                .thenReturn(token);

        AuthResponseDTO resultado = userDetailsService.loginUser(requestDTO);

        assertEquals(username, resultado.username());
        assertEquals("login ok", resultado.message());
        assertEquals(token, resultado.jwt());
        assertTrue(resultado.status());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());

        verify(jwtUtils).createToken(org.mockito.ArgumentMatchers.any(Authentication.class));
    }

    private UserSec crearUsuarioAdmin() {
        Permission readPermission = new Permission();
        readPermission.setId(1L);
        readPermission.setPermissionName("READ");

        Permission createPermission = new Permission();
        createPermission.setId(2L);
        createPermission.setPermissionName("CREATE");

        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRole("ADMIN");
        adminRole.setPermissionsList(Set.of(readPermission, createPermission));

        UserSec userSec = new UserSec();
        userSec.setId(1L);
        userSec.setUsername("admin");
        userSec.setPassword("password-encriptada");
        userSec.setEnabled(true);
        userSec.setAccountNotExpired(true);
        userSec.setAccountNotLocked(true);
        userSec.setCredentialNotExpired(true);
        userSec.setRoleList(Set.of(adminRole));

        return userSec;
    }
}
