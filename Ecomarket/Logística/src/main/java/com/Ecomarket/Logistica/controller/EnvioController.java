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

    @GetMapping("/estado/{estado}")
    public List<Envio> buscarPorEstado(@PathVariable String estado) {
        return envioService.buscarPorEstado(estado);
    }

    @GetMapping("/destino/{destino}")
    public List<Envio> buscarPorDestino(@PathVariable String destino) {
        return envioService.buscarPorDestino(destino);
    }

    @PostMapping
    public Envio crearEnvio(@RequestBody Envio envio) {
        return envioService.save(envio);
    }

    @PutMapping("/{idEnvio}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long idEnvio, @RequestBody Envio envio) {
        Envio existente = envioService.findById(idEnvio);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        envio.setIdEnvio(idEnvio);
        return ResponseEntity.ok(envioService.save(envio));
    }

    @DeleteMapping("/{idEnvio}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long idEnvio) {
        envioService.delete(idEnvio);
        return ResponseEntity.noContent().build();
    }
}