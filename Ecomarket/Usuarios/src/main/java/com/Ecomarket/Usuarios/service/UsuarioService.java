package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario existente
    public Usuario actualizarUsuario(Long idUsuario, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuario.setApellidoUsuario(usuarioActualizado.getApellidoUsuario());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setContrasena(usuarioActualizado.getContrasena());
        usuario.setDirUsuario(usuarioActualizado.getDirUsuario());
        usuario.setMetodoPago(usuarioActualizado.getMetodoPago());
        return usuarioRepository.save(usuario);
    }

    // Login (autenticación simple)
    public Usuario login(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena)
                .orElse(null);
    }
   

}
