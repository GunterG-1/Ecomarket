package com.Ecomarket.Usuarios.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Usuario;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion,Long> {
    List<Devolucion> findByUsuarioIdUsuario(Long idUsuario);
    Optional<Devolucion> findByIdDevolucion(Long idDevolucion);
    boolean existsByUsuarioAndIdVentaAndIdProducto(Usuario usuario, Long idVenta, Long idProducto);
}