package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.controller.UsuarioController;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import com.Ecomarket.Usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private RolRepository rolRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistrarUsuario_Exitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistrarUsuario_Error() throws Exception {
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenThrow(new RuntimeException("Correo en uso"));
        mockMvc.perform(post("/api/usuarios/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Usuario())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Exitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@correo.com");
        usuario.setContrasena("1234");
        when(usuarioService.login(eq("test@correo.com"), eq("1234"))).thenReturn(usuario);
        Map<String, String> body = new HashMap<>();
        body.put("correo", "test@correo.com");
        body.put("contrasena", "1234");
        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_Fallo() throws Exception {
        when(usuarioService.login(eq("fail@correo.com"), eq("wrong"))).thenReturn(null);
        Map<String, String> body = new HashMap<>();
        body.put("correo", "fail@correo.com");
        body.put("contrasena", "wrong");
        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk()); // El controlador retorna 200 aunque el usuario sea null
    }

    @Test
    void testActualizarUsuario_Exitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarUsuario_NoEncontrado() throws Exception {
        when(usuarioService.actualizarUsuario(eq(2L), any(Usuario.class))).thenThrow(new RuntimeException("No encontrado"));
        mockMvc.perform(put("/api/usuarios/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Usuario())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testVerPerfil_Existente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario));
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testVerPerfil_NoExistente() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/usuarios/2"))
                .andExpect(status().isNotFound());
    }
  
@Test
void testSoloAdmin_NoAutorizado() throws Exception {
    Usuario usuario = new Usuario();
    mockMvc.perform(post("/api/usuarios/solo-admin")
            .header("rol", "CLIENTE")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isForbidden());
}

@Test
void testSoloAdmin_Autorizado() throws Exception {
    Usuario usuario = new Usuario();
    mockMvc.perform(post("/api/usuarios/solo-admin")
            .header("rol", "ADMINISTRADOR")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk());
}
}
