package com.Ecomarket.Usuarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Ecomarket.Usuarios.model.Reclamacion;

@Repository
public interface ReclamacionRepository extends JpaRepository<Reclamacion, Long> {
    List<Reclamacion> findByUsuarioIdUsuario(Long idUsuario);
    Optional<Reclamacion> findByIdReclamacion(Long idReclamacion);
}