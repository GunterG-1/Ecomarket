package com.Ecomarket.Usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.Ecomarket.Usuarios.assamblers.AdminModelAssambler;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.service.AdminService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/admin")
public class AdminControllerV2 {
    

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminModelAssambler adminModelAssambler;

    @GetMapping("/usuarios")
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = adminService.listarUsuarios().stream()
            .map(adminModelAssambler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios);
    }

    @GetMapping("/usuarios/{id}")
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = adminService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return adminModelAssambler.toModel(usuario);
    }
    @GetMapping("/usuarios/activos")
    public CollectionModel<EntityModel<Usuario>> getUsuariosActivos() {
        List<EntityModel<Usuario>> usuarios = adminService.listarUsuariosActivos().stream()
            .map(adminModelAssambler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios);
    }

    @GetMapping("/usuarios/desactivados")
    public CollectionModel<EntityModel<Usuario>> getUsuariosDesactivados() {
        List<EntityModel<Usuario>> usuarios = adminService.listarUsuariosDesactivados().stream()
            .map(adminModelAssambler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios);
    }
}
