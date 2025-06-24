package com.Ecomarket.Venta.assamblers;

import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.controller.VentaControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VentaModelAssambler implements RepresentationModelAssembler<Venta, EntityModel<Venta>> {
    @Override
    public @NonNull EntityModel<Venta> toModel(@NonNull Venta venta) {
        return EntityModel.of(venta,
            linkTo(methodOn(VentaControllerV2.class).getVentaById(venta.getIdVenta())).withSelfRel(),
            linkTo(methodOn(VentaControllerV2.class).getAllVentas()).withRel("ventas")
        );
    }
}
