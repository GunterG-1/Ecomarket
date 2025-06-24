package com.Ecomarket.Logistica.assamblers;

import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.controller.EnvioControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioModelAssambler implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {
    @Override
    public @NonNull EntityModel<Envio> toModel(@NonNull Envio envio) {
        return EntityModel.of(envio,
            linkTo(methodOn(EnvioControllerV2.class).getEnvioById(envio.getIdEnvio())).withSelfRel(),
            linkTo(methodOn(EnvioControllerV2.class).getAllEnvios()).withRel("envios")
        );
    }
}
