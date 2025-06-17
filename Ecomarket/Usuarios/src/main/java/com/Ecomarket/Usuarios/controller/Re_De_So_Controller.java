package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.dto.DevolucionResponseDTO;
import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.service.Re_De_So_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio")
public class Re_De_So_Controller {

    @Autowired
    private Re_De_So_service service;

    // --------- DEVOLUCIONES ---------
    @GetMapping("/devoluciones")
    public List<Devolucion> listarDevoluciones() {
        return service.listarDevoluciones();
    }

    @PostMapping("/crear/devoluciones")
    public ResponseEntity<DevolucionResponseDTO> crearDevolucion(@RequestBody Devolucion devolucion) {
        DevolucionResponseDTO response = service.crearDevolucion(devolucion);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/devo/actulaizar/{idDevolucion}")
    public ResponseEntity<Devolucion> actualizarDevolucion(@PathVariable("idDevolucion") Long idDevolucion, @RequestBody Devolucion nueva) {
        return ResponseEntity.ok(service.actualizarDevolucion(idDevolucion, nueva));
    }

    @DeleteMapping("/devoluciones/{idDevolucion}")
    public ResponseEntity<Void> eliminarDevolucion(@PathVariable("idDevolucion") Long idDevolucion) {
        service.eliminarDevolucion(idDevolucion);
        return ResponseEntity.noContent().build();
    }

    // --------- RECLAMACIONES ---------
    @GetMapping("/reclamaciones")
    public List<Reclamacion> listarReclamaciones() {
        return service.listarReclamaciones();
    }

    @PostMapping("/crear/reclamaciones")
    public ResponseEntity<Reclamacion> crearReclamacion(@RequestBody Reclamacion reclamacion) {
        return ResponseEntity.ok(service.crearReclamacion(reclamacion));
    }

    @PutMapping("/recla/actualizar/{idReclamos}")
    public ResponseEntity<Reclamacion> actualizarReclamacion(@PathVariable("idReclamos") Long idReclamos, @RequestBody Reclamacion nueva) {
        return ResponseEntity.ok(service.actualizarReclamacion(idReclamos, nueva));
    }

    @DeleteMapping("/reclamaciones/{idReclamos}")
    public ResponseEntity<Void> eliminarReclamacion(@PathVariable("idReclamos") Long idReclamos) {
        service.eliminarReclamacion(idReclamos);
        return ResponseEntity.noContent().build();
    }

    // --------- SOLICITUDES DE SOPORTE ---------
    @GetMapping("/soporte")
    public List<SolicitudSoporte> listarSolicitudesSoporte() {
        return service.listarSolicitudesSoporte();
    }

    @PostMapping("/crear/soporte")
    public ResponseEntity<SolicitudSoporte> crearSolicitudSoporte(@RequestBody SolicitudSoporte solicitud) {
        return ResponseEntity.ok(service.crearSolicitudSoporte(solicitud));
    }

    @PutMapping("/soporte/actualizar/{idSolicitud}")
    public ResponseEntity<SolicitudSoporte> actualizarSolicitudSoporte(@PathVariable("idSolicitud") Long idSolicitud, @RequestBody SolicitudSoporte nueva) {
        return ResponseEntity.ok(service.actualizarSolicitudSoporte(idSolicitud, nueva));
    }

    @DeleteMapping("/soporte/{idSolicitud}")
    public ResponseEntity<Void> eliminarSolicitudSoporte(@PathVariable("idSolicitud") Long idSolicitud) {
        service.eliminarSolicitudSoporte(idSolicitud);
        return ResponseEntity.noContent().build();
    }
}
