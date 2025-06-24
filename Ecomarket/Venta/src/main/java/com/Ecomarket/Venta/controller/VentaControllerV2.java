package com.Ecomarket.Venta.controller;

import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.service.VentaService;
import com.Ecomarket.Venta.assamblers.VentaModelAssambler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/ventas")
public class VentaControllerV2 {
    @Autowired
    private VentaService ventaService;
    @Autowired
    private VentaModelAssambler assembler;

    @GetMapping("/{idVenta}")
    public EntityModel<Venta> getVentaById(@PathVariable Long idVenta) {
        Venta venta = ventaService.listarVentas().stream()
            .filter(v -> v.getIdVenta().equals(idVenta))
            .findFirst().orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return assembler.toModel(venta);
    }

    @GetMapping
    public CollectionModel<EntityModel<Venta>> getAllVentas() {
        List<EntityModel<Venta>> ventas = ventaService.listarVentas().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(ventas);
    }
}
