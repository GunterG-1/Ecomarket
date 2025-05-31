package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }


    // Crear un nuevo usuario con rol
    public Usuario crearUsuarioConRol(Usuario usuario, String nombreRol) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        Rol rol = rolRepository.findByNombreRol(nombreRol);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado");
        }
        usuario.getRoles().add(rol);
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

    // Activar o desactivar usuario
    public Usuario cambiarEstadoUsuario(Long idUsuario, boolean activo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(idUsuario);
    }
      public Optional<Usuario> buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }
}

