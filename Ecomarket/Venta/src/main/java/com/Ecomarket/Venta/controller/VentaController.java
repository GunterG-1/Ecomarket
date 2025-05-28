package com.Ecomarket.Venta.controller;

import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Listar todas las ventas
    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listarVentas();
    }

    // Registrar una venta (con o sin cupón)
    @PostMapping("/registrarVenta")
    public ResponseEntity<Venta> registrarVenta(
            @RequestBody Venta venta,
            @RequestParam(value = "codigo", required = false) String codigo) {
        try {
            Venta ventaGuardada = ventaService.registrarVenta(venta, codigo);
            return ResponseEntity.status(201).body(ventaGuardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar venta por id
    @GetMapping("/{idVenta}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long idVenta) {
        Venta venta = ventaService.obtenerVentaPorId(idVenta);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar ventas por correo
    @GetMapping("/correo/{correo}")
    public List<Venta> buscarPorCorreo(@PathVariable String correo) {
        return ventaService.buscarPorCorreo(correo);
    }

    // Buscar ventas por nombre de usuario
    @GetMapping("/usuario/{nombreUsuario}")
    public List<Venta> buscarPorNombreUsuario(@PathVariable String nombreUsuario) {
        return ventaService.buscarPorNombreUsuario(nombreUsuario);
    }
}
