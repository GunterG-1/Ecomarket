package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.service.UsuarioService;
import com.Ecomarket.Usuarios.assamblers.UsuarioModelAssambler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioModelAssambler assembler;

    @GetMapping("/{idUsuario}")
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.listarUsuarios().stream()
            .filter(u -> u.getIdUsuario().equals(idUsuario))
            .findFirst().orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return assembler.toModel(usuario);
    }

    @GetMapping
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.listarUsuarios().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios);
    }
}
