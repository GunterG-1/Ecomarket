package com.Ecomarket.Producto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Listar todos los productos
    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    // Buscar producto por ID
    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long idProducto) {
        Optional<Producto> producto = productoService.findById(idProducto);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Agregar producto
    @PostMapping("/crearProducto")
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        try {
            Producto nuevo = productoService.agregarProducto(producto);
            return ResponseEntity.status(201).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // Actualizar stock de un producto
    @PutMapping("/{idProducto}/actualizarStock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable Long idProducto,
            @RequestParam int cantidadVendida) {
        try {
            productoService.actualizarStock(idProducto, cantidadVendida);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar producto
    @PutMapping("/actualizar/{idProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long idProducto, @RequestBody Producto productoActualizado) {
        try {
            Producto actualizado = productoService.actualizarProducto(idProducto, productoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar producto
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long idProducto) {
        productoService.eliminarProducto(idProducto);
        return ResponseEntity.noContent().build();
    }
}