package com.Ecomarket.Usuarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecomarket.Usuarios.model.SolicitudSoporte;

@Repository
public interface SolicitudSoporteRepository extends JpaRepository<SolicitudSoporte, Long> {
    List<SolicitudSoporte> findByUsuarioIdUsuario(Long idUsuario);
    Optional<SolicitudSoporte> findById(Long idSolicitudSoporte);
}
