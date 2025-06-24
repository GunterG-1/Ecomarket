package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.service.Re_De_So_service;
import com.Ecomarket.Usuarios.assamblers.Re_De_So_ModelAssambler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/Servicios")
public class Re_De_So_ControllerV2 {
    @Autowired
    private Re_De_So_service service;
    @Autowired
    private Re_De_So_ModelAssambler assembler;

    // DEVOLUCIONES
    @GetMapping("/devoluciones/{id}")
    public EntityModel<Devolucion> getDevolucionById(@PathVariable Long id) {
        Devolucion d = service.listarDevoluciones().stream()
            .filter(dev -> dev.getIdDevolucion().equals(id))
            .findFirst().orElseThrow(() -> new RuntimeException("Devolución no encontrada"));
        return assembler.toModel(d);
    }

    @GetMapping("/devoluciones")
    public CollectionModel<EntityModel<Devolucion>> getAllDevoluciones() {
        List<EntityModel<Devolucion>> list = service.listarDevoluciones().stream()
            .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(list);
    }

    // RECLAMACIONES
    @GetMapping("/reclamaciones/{id}")
    public EntityModel<Reclamacion> getReclamacionById(@PathVariable Long id) {
        Reclamacion r = service.listarReclamaciones().stream()
            .filter(rec -> rec.getIdReclamacion().equals(id))
            .findFirst().orElseThrow(() -> new RuntimeException("Reclamación no encontrada"));
        return assembler.toModel(r);
    }

    @GetMapping("/reclamaciones")
    public CollectionModel<EntityModel<Reclamacion>> getAllReclamaciones() {
        List<EntityModel<Reclamacion>> list = service.listarReclamaciones().stream()
            .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(list);
    }

    // SOLICITUDES DE SOPORTE
    @GetMapping("/solicitudes/{id}")
    public EntityModel<SolicitudSoporte> getSolicitudSoporteById(@PathVariable Long id) {
        SolicitudSoporte s = service.listarSolicitudesSoporte().stream()
            .filter(sol -> sol.getIdSolicitud().equals(id))
            .findFirst().orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return assembler.toModel(s);
    }

    @GetMapping("/solicitudes")
    public CollectionModel<EntityModel<SolicitudSoporte>> getAllSolicitudesSoporte() {
        List<EntityModel<SolicitudSoporte>> list = service.listarSolicitudesSoporte().stream()
            .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(list);
    }
}
