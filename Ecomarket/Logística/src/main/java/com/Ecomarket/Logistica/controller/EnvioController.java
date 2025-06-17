package com.Ecomarket.Logistica.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.service.EnvioService;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public List<Envio> listarEnvios() {
        return envioService.listarEnvios();
    }

    @GetMapping("/{idEnvio}")
    public ResponseEntity<Envio> obtenerEnvio(@PathVariable Long idEnvio) {
        Optional<Envio> envio = envioService.obtenerEnvio(idEnvio);
        return envio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @PostMapping("/crearEnvio")
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        try{
            Envio envioNuevo = envioService.crearEnvio(envio);
            return ResponseEntity.ok(envioNuevo);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/actualizar/{idEnvio}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long idEnvio, @RequestBody Envio envio) {
        try {
            Envio actualizado = envioService.actualizarEnvio(idEnvio, envio);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idEnvio}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long idEnvio) {
        envioService.delete(idEnvio);
        return ResponseEntity.noContent().build();
    }
}