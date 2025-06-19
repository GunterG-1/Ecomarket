package com.Ecomarket.Venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecomarket.Venta.model.Venta;

public interface VentaRepository extends JpaRepository <Venta, Long>{
List<Venta> findByIdUsuario(Long idUsuario);

}
