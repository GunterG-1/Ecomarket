package com.Ecomarket.Usuarios.assamblers;

import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.controller.Re_De_So_ControllerV2;
import org.springframework.hateoas.EntityModel;

import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class Re_De_So_ModelAssambler {
    public @NonNull EntityModel<Devolucion> toModel(@NonNull Devolucion devolucion) {
        return EntityModel.of(devolucion,
            linkTo(methodOn(Re_De_So_ControllerV2.class).getDevolucionById(devolucion.getIdDevolucion())).withSelfRel(),
            linkTo(methodOn(Re_De_So_ControllerV2.class).getAllDevoluciones()).withRel("devoluciones")
        );
    }

    public @NonNull EntityModel<Reclamacion> toModel(@NonNull Reclamacion reclamacion) {
        return EntityModel.of(reclamacion,
            linkTo(methodOn(Re_De_So_ControllerV2.class).getReclamacionById(reclamacion.getIdReclamacion())).withSelfRel(),
            linkTo(methodOn(Re_De_So_ControllerV2.class).getAllReclamaciones()).withRel("reclamaciones")
        );
    }

    public @NonNull EntityModel<SolicitudSoporte> toModel(@NonNull SolicitudSoporte soporte) {
        return EntityModel.of(soporte,
            linkTo(methodOn(Re_De_So_ControllerV2.class).getSolicitudSoporteById(soporte.getIdSolicitud())).withSelfRel(),
            linkTo(methodOn(Re_De_So_ControllerV2.class).getAllSolicitudesSoporte()).withRel("solicitudesSoporte")
        );
    }
}
