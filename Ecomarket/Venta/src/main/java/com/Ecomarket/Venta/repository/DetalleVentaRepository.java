package com.Ecomarket.Venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ecomarket.Venta.model.DetalleVenta;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    // Buscar detalles por id de venta
    List<DetalleVenta> findByVentaIdVenta(Long idVenta);


   
}