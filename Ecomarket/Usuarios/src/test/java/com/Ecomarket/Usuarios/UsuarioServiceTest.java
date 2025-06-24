package com.Ecomarket.Usuarios;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import com.Ecomarket.Usuarios.service.UsuarioService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;



@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private UsuarioService usuarioService ;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertEquals(2, resultado.size());
    }

    @Test
    void testRegistrarUsuario_CorreoYaExiste() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("existente@duoc.cl");
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> usuarioService.registrarUsuario(usuario));
    }

    @Test
    void testRegistrarUsuario_Exitoso() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("nuevo@duoc.cl");
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario resultado = usuarioService.registrarUsuario(usuario);
        assertNotNull(resultado);
        assertTrue(resultado.isActivo());
    }

    @Test
    void testActualizarUsuario_Existente() {
        Usuario existente = new Usuario();
        existente.setIdUsuario(1L);
        Usuario actualizado = new Usuario();
        actualizado.setNombreUsuario("Nuevo");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);
        Usuario result = usuarioService.actualizarUsuario(1L, actualizado);
        assertEquals("Nuevo", result.getNombreUsuario());
    }

    @Test
    void testActualizarUsuario_NoExistente() {
        Usuario actualizado = new Usuario();
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.actualizarUsuario(2L, actualizado));
    }

    @Test
    void testLogin_Exitoso() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("correo@duoc.cl");
        usuario.setContrasena("1234");
        when(usuarioRepository.findByCorreoAndContrasena("correo@duoc.cl", "1234"))
            .thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.login("correo@duoc.cl", "1234");
        assertNotNull(resultado);
    }

    @Test
    void testLogin_Fallido() {
        when(usuarioRepository.findByCorreoAndContrasena("correo@duoc.cl", "incorrecta"))
            .thenReturn(Optional.empty());
        Usuario resultado = usuarioService.login("correo@duoc.cl", "incorrecta");
        assertNull(resultado);
    }
}