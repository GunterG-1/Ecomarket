package com.Ecomarket.Venta.service;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.Ecomarket.Venta.client.ProductoClient;
import com.Ecomarket.Venta.client.ProductoClient.ProductoDTO;
import com.Ecomarket.Venta.client.UsuarioClient;
import com.Ecomarket.Venta.client.UsuarioClient.UsuarioDTO;
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
    private UsuarioClient usuarioClient;

    

   

    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta) {
        // Obtener datos del usuario
        UsuarioDTO usuario = usuarioClient.obtenerPorId(venta.getIdUsuario());
        
        venta.setNombreUsuario(usuario.getNombreUsuario());
        venta.setApellidoUsuario(usuario.getApellidoUsuario());
        venta.setCorreo(usuario.getCorreo());
        venta.setDirUsuario(usuario.getDirUsuario());

        // Procesar los detalles de venta
        for (DetalleVenta detalle : venta.getDetalle()) {
        
           
            // Obtener datos del producto
            ProductoDTO producto = productoClient.obtenerProducto(detalle.getIdProducto());
            if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + detalle.getIdProducto());
}
            

            detalle.setNombreProducto(producto.getNombreProducto());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setVenta(venta);
        }
            // Guardar detalle
            Venta ventaGuardada = ventaRepository.save(venta);
            
            // Actualizar stock del producto
        for (DetalleVenta d : venta.getDetalle()) {
        productoClient.actualizarStock(d.getIdProducto(), d.getCantidad());
        
    }
        
    return ventaGuardada;

    }

    
    public Optional<Venta> findById(Long idVenta){
        return ventaRepository.findById(idVenta);
    }
    public Venta actualizarVenta(Long idVenta, Venta ventaActualizada) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("venta no encontrada"));

        for (int i = 0; i < venta.getDetalle().size(); i++) {
            DetalleVenta detalleExistente = venta.getDetalle().get(i);
            DetalleVenta detalleNuevo = ventaActualizada.getDetalle().get(i);

            // 1. Revertir el stock anterior
            productoClient.actualizarStock(detalleExistente.getIdProducto(), -detalleExistente.getCantidad());

            // 2. Actualizar los datos del detalle
            detalleExistente.setIdProducto(detalleNuevo.getIdProducto());
            detalleExistente.setCantidad(detalleNuevo.getCantidad());

            // 3. Aplicar el nuevo descuento de stock
            productoClient.actualizarStock(detalleExistente.getIdProducto(), detalleExistente.getCantidad());
        }

        return ventaRepository.save(venta);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

   
}

