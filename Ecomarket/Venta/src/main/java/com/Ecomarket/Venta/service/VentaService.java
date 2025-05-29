package com.Ecomarket.Venta.service;



import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.Ecomarket.Venta.client.ProductoClient;
import com.Ecomarket.Venta.model.DetalleVenta;
import com.Ecomarket.Venta.model.Venta;

import com.Ecomarket.Venta.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private DetalleVenta detalleVenta;

   

    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta) {
        Venta nuevaVenta = venta;
        return ventaRepository.save(nuevaVenta);
        
         venta.getDetalles().forEach(d -> {
            // 1. Obtener datos reales del producto
            ProductoClient.ProductoDTO producto = productoClient.obtenerProductoPorId(d.getIdProducto());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + d.getIdProducto());
            }
            if (producto.getStock() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }
            // 2. Usar el precio y nombre real del producto
            d.setPrecioUnitario(producto.getPrecioUnitario());
            d.setNombreProducto(producto.getNombreProducto());
            d.setTotal(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())));
            d.setVenta(venta);

            // 3. Actualizar stock en Producto
            productoClient.actualizarStock(d.getIdProducto(), d.getCantidad());
    }
    
   

    
    public Optional<Venta> obtenerVenta(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    // Comunicación con microservicio Producto a través del gateway
  
}

