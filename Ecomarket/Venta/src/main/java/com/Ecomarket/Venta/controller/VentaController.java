package com.Ecomarket.Venta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.repository.VentaRepository;
import com.Ecomarket.Venta.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> ventas = ventaService.listarVentas();
        if(ventas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }


    @PostMapping("/nueva")
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.registrarVenta(venta);
            return ResponseEntity.ok(nuevaVenta);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{idVenta}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long idVenta) {
        return ventaService.findById(idVenta)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{idVenta}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Long idVenta, @RequestBody Venta ventaActualizada) {
        try {
            Venta actualizado = ventaService.actualizarVenta(idVenta, ventaActualizada);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
    }
}

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long idVenta) {
        if (ventaService.findById(idVenta).isPresent()) {
            ventaService.delete(idVenta);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/usuario/{idUsuario}")
    public List<Venta> obtenerVentasPorUsuario(@PathVariable Long idUsuario) {
        return ventaRepository.findByIdUsuario(idUsuario);
    }
}
