package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.Ecomarket.Usuarios.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private RolRepository rolRepository;

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> resultado = adminService.listarUsuarios();
        assertEquals(2, resultado.size());
    }

    @Test
    void testCrearUsuarioConRol_Exitoso() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("nuevo@duoc.cl");
        usuario.setRoles(new HashSet<>());
        Rol rol = new Rol();
        rol.setNombreRol("ADMINISTRADOR");
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(rolRepository.findByNombreRol("ADMINISTRADOR")).thenReturn(rol);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario resultado = adminService.crearUsuarioConRol(usuario, "ADMINISTRADOR");
        assertNotNull(resultado);
        assertTrue(resultado.isActivo());
        assertTrue(resultado.getRoles().contains(rol));
    }

    @Test
    void testCrearUsuarioConRol_CorreoExistente() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("existente@duoc.cl");
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> adminService.crearUsuarioConRol(usuario, "ADMIN"));
    }

    @Test
    void testCrearUsuarioConRol_RolNoEncontrado() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("nuevo@duoc.cl");
        usuario.setRoles(new HashSet<>());
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(rolRepository.findByNombreRol("NOEXISTE")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> adminService.crearUsuarioConRol(usuario, "NOEXISTE"));
    }

    @Test
    void testActualizarUsuario_Existente() {
        Usuario existente = new Usuario();
        existente.setIdUsuario(1L);
        Usuario actualizado = new Usuario();
        actualizado.setNombreUsuario("Nuevo");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);
        Usuario result = adminService.actualizarUsuario(1L, actualizado);
        assertEquals("Nuevo", result.getNombreUsuario());
    }

    @Test
    void testActualizarUsuario_NoExistente() {
        Usuario actualizado = new Usuario();
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> adminService.actualizarUsuario(2L, actualizado));
    }

    @Test
    void testCambiarEstadoUsuario_Existente() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setActivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario result = adminService.cambiarEstadoUsuario(1L, true);
        assertTrue(result.isActivo());
    }

    @Test
    void testCambiarEstadoUsuario_NoExistente() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> adminService.cambiarEstadoUsuario(2L, true));
    }

    @Test
    void testEliminarUsuario_Existente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);
        assertDoesNotThrow(() -> adminService.eliminarUsuario(1L));
    }

    @Test
    void testEliminarUsuario_NoExistente() {
        when(usuarioRepository.existsById(2L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> adminService.eliminarUsuario(2L));
    }

    @Test
    void testBuscarPorId_Existente() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = adminService.buscarPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdUsuario());
    }

    @Test
    void testBuscarPorId_NoExistente() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Usuario> result = adminService.buscarPorId(2L);
        assertFalse(result.isPresent());
    }
}
