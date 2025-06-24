package com.Ecomarket.Usuarios.assamblers;

import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.controller.AdminControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AdminModelAssambler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {
    @Override
    public @NonNull EntityModel<Usuario> toModel(@NonNull Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(AdminControllerV2.class).getUsuarioById(usuario.getIdUsuario())).withSelfRel(),
            linkTo(methodOn(AdminControllerV2.class).getAllUsuarios()).withRel("usuarios"),
            linkTo(methodOn(AdminControllerV2.class).getUsuariosActivos()).withRel("usuarios-activos"),
            linkTo(methodOn(AdminControllerV2.class).getUsuariosDesactivados()).withRel("usuarios-desactivados")
        );
    }
}
