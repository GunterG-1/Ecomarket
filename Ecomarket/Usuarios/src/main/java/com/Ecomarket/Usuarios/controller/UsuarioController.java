package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Registrar usuario
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String contrasena = body.get("contrasena");
        Usuario usuario = usuarioService.login(correo, contrasena);
        return ResponseEntity.ok(usuario);
    }

    // Actualizar usuario
    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long idUsuario, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(idUsuario, usuarioActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ver perfil
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> verPerfil(@PathVariable Long idUsuario) {
        Optional<Usuario> usuario = usuarioService.listarUsuarios().stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst();
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/solo-admin")
public ResponseEntity<?> soloAdmin(@RequestBody Usuario usuario, @RequestHeader String rol) {
    if (!"ADMINISTRADOR".equals(rol)) {
        return ResponseEntity.status(403).body("No autorizado");
    }
    return ResponseEntity.ok("Acceso permitido");
}

}
