package com.Ecomarket.Venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ecomarket.Venta.model.Cupon;

import java.util.List;

public interface CuponRepository extends JpaRepository<Cupon, String> {

    // Buscar cupones activos
    List<Cupon> findByActivoTrue();

}
