package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.dto.UsuarioConRolRequest;
import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.service.AdminService;
import com.Ecomarket.Usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RolRepository rolRepository;

    // Listar todos los usuarios
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return adminService.listarUsuarios();
    }

    // Crear usuario y asignar rol
    @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioConRolRequest request) {
        Rol rol = rolRepository.findByNombreRol(request.getNombreRol());
        if (rol == null) {
            return ResponseEntity.badRequest().build();
        }
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoUsuario(request.getApellidoUsuario());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(request.getContrasena());
        usuario.setDirUsuario(request.getDirUsuario());
        usuario.setMetodoPago(request.getMetodoPago());
        usuario.getRoles().add(rol);
        Usuario creado = adminService.crearUsuarioConRol(usuario, request.getNombreRol());
        return ResponseEntity.ok(creado);
    }

    // Actualizar usuario
    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable("idUsuario") Long idUsuario,
            @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario actualizado = adminService.actualizarUsuario(idUsuario, usuarioActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar o desactivar usuario
    @PutMapping("/{idUsuario}/estado")
    public ResponseEntity<?> cambiarEstadoUsuario(
            @PathVariable Long idUsuario,
            @RequestBody Map<String, Boolean> body) {
        boolean activo = body.get("activo");
        adminService.cambiarEstadoUsuario(idUsuario, activo);
        return ResponseEntity.ok().build();
    }

    // Eliminar usuario
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("idUsuario") Long idUsuario) {
        try {
            adminService.eliminarUsuario(idUsuario);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
     @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long idUsuario) {
        return adminService.buscarPorId(idUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
