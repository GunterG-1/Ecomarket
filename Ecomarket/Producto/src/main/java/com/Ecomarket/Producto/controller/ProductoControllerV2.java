package com.Ecomarket.Producto.controller;

import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.service.ProductoService;
import com.Ecomarket.Producto.assamblers.ProductoModelAssambler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerV2 {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoModelAssambler assembler;

    @GetMapping("/{idProducto}")
    public EntityModel<Producto> getProductoById(@PathVariable Long idProducto) {
        Producto producto = productoService.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return assembler.toModel(producto);
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> getAllProductos() {
        List<EntityModel<Producto>> productos = productoService.listarProductos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(productos);
    }

    @GetMapping("/codigo/{codigo}")
    public EntityModel<Producto> getProductoByCodigo(@PathVariable String codigo) {
        Optional<Producto> productoOpt = productoService.listarProductos().stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst();
        Producto producto = productoOpt.orElseThrow(() -> new RuntimeException("Producto no encontrado por código"));
        return assembler.toModel(producto);
    }
}
