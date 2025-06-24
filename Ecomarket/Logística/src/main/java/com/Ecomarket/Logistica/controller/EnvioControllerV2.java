package com.Ecomarket.Logistica.controller;

import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.service.EnvioService;
import com.Ecomarket.Logistica.assamblers.EnvioModelAssambler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/envios")
public class EnvioControllerV2 {
    @Autowired
    private EnvioService envioService;
    @Autowired
    private EnvioModelAssambler assembler;

    @GetMapping("/{idEnvio}")
    public EntityModel<Envio> getEnvioById(@PathVariable Long idEnvio) {
        Envio envio = envioService.listarEnvios().stream()
            .filter(e -> e.getIdEnvio().equals(idEnvio))
            .findFirst().orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        return assembler.toModel(envio);
    }

    @GetMapping
    public CollectionModel<EntityModel<Envio>> getAllEnvios() {
        List<EntityModel<Envio>> envios = envioService.listarEnvios().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(envios);
    }
}
