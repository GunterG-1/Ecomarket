package com.Ecomarket.Venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ecomarket.Venta.model.Venta;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar ventas por correo de usuario
    List<Venta> findByCorreo(String correo);

    // Buscar ventas por nombre de usuario
    List<Venta> findByNombreUsuario(String nombreUsuario);
}
