package com.Ecomarket.Venta;


import com.Ecomarket.Venta.client.ProductoClient;
import com.Ecomarket.Venta.client.UsuarioClient;
import com.Ecomarket.Venta.model.*;
import com.Ecomarket.Venta.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

     @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioClient usuarioClient;
    @Autowired
    private ProductoClient productoClient;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        UsuarioClient.UsuarioDTO[] usuarios = usuarioClient.obtenerTodos();
        ProductoClient.ProductoDTO[] productos = productoClient.obtenerTodos();
        
        List<Venta> ventasCreadas = new ArrayList<>();
        
        for (UsuarioClient.UsuarioDTO usuario : usuarios) {
            if (usuario == null) continue; // Si el usuario no existe, salta
            if (usuario.getActivo() == null || !usuario.getActivo()) continue; 
            if (ventaRepository.existsByIdUsuario(usuario.getIdUsuario())) continue;
            
            Venta venta = new Venta();
            venta.setIdUsuario(usuario.getIdUsuario());
            venta.setNombreUsuario(usuario.getNombreUsuario());
            venta.setApellidoUsuario(usuario.getApellidoUsuario());
            venta.setCorreo(usuario.getCorreo());
            venta.setDirUsuario(usuario.getDirUsuario());
            venta.setFechaVenta(new Date());

            // Detalles de venta
            List<DetalleVenta> detalles = new ArrayList<>();
            int cantidadDetalles = faker.number().numberBetween(1, 3);
            for (int j = 0; j < cantidadDetalles; j++) {
               ProductoClient.ProductoDTO producto = productos[random.nextInt(productos.length)];
                if (producto == null) continue; // Si el producto no existe, salta

                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdProducto(producto.getIdProducto());
                detalle.setNombreProducto(producto.getNombreProducto());
                detalle.setCantidad(faker.number().numberBetween(1, 5));
                detalle.setPrecioUnitario(producto.getPrecioUnitario() != null ? producto.getPrecioUnitario() : BigDecimal.valueOf(faker.number().randomDouble(2, 100, 10000)));
                detalle.setVenta(venta);
                detalles.add(detalle);
            }
            venta.setDetalle(detalles);

            ventaRepository.save(venta);
            ventasCreadas.add(venta);
        }

        // Imprimir ventas generadas
        System.out.println("=== VENTAS GENERADAS ===");
        for (Venta v : ventasCreadas) {
            System.out.println(
                "ID: " + v.getIdVenta() +
                " | UsuarioID: " + v.getIdUsuario() +
                " | Nombre: " + v.getNombreUsuario() +
                " | Apellido: " + v.getApellidoUsuario() +
                " | Correo: " + v.getCorreo() +
                " | Dirección: " + v.getDirUsuario() +
                " | Fecha: " + v.getFechaVenta()
            );
            if (v.getDetalle() != null) {
                for (DetalleVenta d : v.getDetalle()) {
                    System.out.println("   Detalle -> ProductoID: " + d.getIdProducto() +
                            " | Nombre: " + d.getNombreProducto() +
                            " | Cantidad: " + d.getCantidad() +
                            " | Precio: " + d.getPrecioUnitario());
                }
            }
        }
    }
}