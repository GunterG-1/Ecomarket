package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.controller.AdminController;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.dto.UsuarioConRolRequest;
import com.Ecomarket.Usuarios.service.AdminService;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;
    @MockBean
    private RolRepository rolRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarUsuarios() throws Exception {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(adminService.listarUsuarios()).thenReturn(usuarios);
        mockMvc.perform(get("/api/admin/usuarios/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearUsuario_Exitoso() throws Exception {
        UsuarioConRolRequest request = new UsuarioConRolRequest();
        request.setNombreRol("ADMIN");
        Rol rol = new Rol();
        when(rolRepository.findByNombreRol("ADMIN")).thenReturn(rol);
        Usuario usuario = new Usuario();
        when(adminService.crearUsuarioConRol(any(Usuario.class), eq("ADMIN"))).thenReturn(usuario);
        mockMvc.perform(post("/api/admin/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearUsuario_RolNoEncontrado() throws Exception {
        UsuarioConRolRequest request = new UsuarioConRolRequest();
        request.setNombreRol("NOEXISTE");
        when(rolRepository.findByNombreRol("NOEXISTE")).thenReturn(null);
        mockMvc.perform(post("/api/admin/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarUsuario_Exitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(adminService.actualizarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(put("/api/admin/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarUsuario_NoEncontrado() throws Exception {
        when(adminService.actualizarUsuario(eq(2L), any(Usuario.class))).thenThrow(new RuntimeException("No encontrado"));
        mockMvc.perform(put("/api/admin/usuarios/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Usuario())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCambiarEstadoUsuario() throws Exception {
        when(adminService.cambiarEstadoUsuario(eq(1L), eq(true))).thenReturn(new Usuario());
        Map<String, Boolean> body = new HashMap<>();
        body.put("activo", true);
        mockMvc.perform(put("/api/admin/usuarios/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarUsuario_Exitoso() throws Exception {
        doNothing().when(adminService).eliminarUsuario(1L);
        mockMvc.perform(delete("/api/admin/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarUsuario_NoEncontrado() throws Exception {
        doThrow(new RuntimeException("No encontrado")).when(adminService).eliminarUsuario(2L);
        mockMvc.perform(delete("/api/admin/usuarios/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPorId_Existente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(adminService.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        mockMvc.perform(get("/api/admin/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorId_NoExistente() throws Exception {
        when(adminService.buscarPorId(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/admin/usuarios/2"))
                .andExpect(status().isNotFound());
    }
}
