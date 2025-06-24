package com.Ecomarket.Producto.assamblers;

import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.controller.ProductoControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssambler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {
    @Override
    public @NonNull EntityModel<Producto> toModel(@NonNull Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerV2.class).getProductoById(producto.getIdProducto())).withSelfRel(),
            linkTo(methodOn(ProductoControllerV2.class).getAllProductos()).withRel("productos"),
            linkTo(methodOn(ProductoControllerV2.class).getProductoByCodigo(producto.getCodigo())).withRel("buscarPorCodigo")
        );
    }
}
