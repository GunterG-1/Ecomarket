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
import com.Ecomarket.Venta.repository.DetalleVentaRepository;

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

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

   

    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta) {
        // Obtener datos del usuario
        UsuarioDTO usuario = usuarioClient.obtenerPorId(venta.getIdUsuario());
         System.out.println("Datos usuario obtenidos: " + usuario);
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
        System.out.println("Stock actualizado para producto ID: " + d.getIdProducto());
    }
        
    return ventaGuardada;

    }

    
    public Optional<Venta> findById(Long idVenta){
        return ventaRepository.findById(idVenta);
    }
    public Venta actualizarVenta(Long idVenta, Venta ventaActualizada){
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("venta no encontrado"));
        venta.setNombreUsuario(ventaActualizada.getNombreUsuario());
        venta.setApellidoUsuario(ventaActualizada.getApellidoUsuario());
        venta.setCorreo(ventaActualizada.getCorreo());
        venta.setDirUsuario(ventaActualizada.getDirUsuario());
        venta.setFechaVenta(ventaActualizada.getFechaVenta());
        venta.setDetalle(ventaActualizada.getDetalle());

        return ventaRepository.save(venta);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

   
}

