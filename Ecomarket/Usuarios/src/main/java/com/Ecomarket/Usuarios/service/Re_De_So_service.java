package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.repository.DevolucionRepository;
import com.Ecomarket.Usuarios.repository.ReclamacionRepository;
import com.Ecomarket.Usuarios.repository.SolicitudSoporteRepository;
import com.Ecomarket.Usuarios.client.ProductoClient;
import com.Ecomarket.Usuarios.client.ProductoClient.ProductoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Re_De_So_service {

    @Autowired
    private DevolucionRepository devolucionRepository;

    @Autowired
    private ReclamacionRepository reclamacionRepository;

    @Autowired
    private SolicitudSoporteRepository solicitudSoporteRepository;

    @Autowired
    private ProductoClient productoClient;

    // --------- DEVOLUCION ---------
    public List<Devolucion> listarDevoluciones() {
        return devolucionRepository.findAll();
    }

    public List<Devolucion> listarDevolucionesPorUsuario(Long idUsuario) {
        return devolucionRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Devolucion crearDevolucion(Devolucion devolucion) {
        // Obtener el nombre del producto desde el microservicio de productos
        ProductoDTO producto = productoClient.obtenerProducto(devolucion.getIdProducto());
        if (producto != null) {
            devolucion.setNombreProducto(producto.getNombreProducto());
        }
        return devolucionRepository.save(devolucion);
    }

    public Devolucion actualizarDevolucion(Long idDevolucion, Devolucion nuevaDevolucion) {
        Devolucion dev = devolucionRepository.findById(idDevolucion)
                .orElseThrow(() -> new RuntimeException("Devolución no encontrada"));
        dev.setMotivo(nuevaDevolucion.getMotivo());
        dev.setIdProducto(nuevaDevolucion.getIdProducto());
        dev.setFechaDevolucion(nuevaDevolucion.getFechaDevolucion());
        dev.setUsuario(nuevaDevolucion.getUsuario());
        dev.setEstado(nuevaDevolucion.getEstado());

        // Actualizar el nombre del producto si cambió el idProducto
        ProductoDTO producto = productoClient.obtenerProducto(nuevaDevolucion.getIdProducto());
        if (producto != null) {
            dev.setNombreProducto(producto.getNombreProducto());
        }

        return devolucionRepository.save(dev);
    }

    public void eliminarDevolucion(Long idDevolucion) {
        devolucionRepository.deleteById(idDevolucion);
    }

    // --------- RECLAMACION ---------
    public List<Reclamacion> listarReclamaciones() {
        return reclamacionRepository.findAll();
    }

    public List<Reclamacion> listarReclamacionesPorUsuario(Long idUsuario) {
        return reclamacionRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Reclamacion crearReclamacion(Reclamacion reclamacion) {
        return reclamacionRepository.save(reclamacion);
    }

    public Reclamacion actualizarReclamacion(Long idReclamacion, Reclamacion nuevaReclamacion) {
        Reclamacion rec = reclamacionRepository.findById(idReclamacion)
                .orElseThrow(() -> new RuntimeException("Reclamación no encontrada"));
        rec.setAsunto(nuevaReclamacion.getAsunto());
        rec.setMensaje(nuevaReclamacion.getMensaje());
        rec.setFechaReclamo(nuevaReclamacion.getFechaReclamo());
        rec.setUsuario(nuevaReclamacion.getUsuario());
        rec.setEstado(nuevaReclamacion.getEstado());
        return reclamacionRepository.save(rec);
    }

    public void eliminarReclamacion(Long idReclamacion) {
        reclamacionRepository.deleteById(idReclamacion);
    }

    // --------- SOLICITUD SOPORTE ---------
    public List<SolicitudSoporte> listarSolicitudesSoporte() {
        return solicitudSoporteRepository.findAll();
    }

    public List<SolicitudSoporte> listarSolicitudesSoportePorUsuario(Long idUsuario) {
        return solicitudSoporteRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public SolicitudSoporte crearSolicitudSoporte(SolicitudSoporte solicitud) {
        return solicitudSoporteRepository.save(solicitud);
    }

    public SolicitudSoporte actualizarSolicitudSoporte(Long idSolicitudSoporte, SolicitudSoporte nuevaSolicitud) {
        SolicitudSoporte sol = solicitudSoporteRepository.findById(idSolicitudSoporte)
                .orElseThrow(() -> new RuntimeException("Solicitud de soporte no encontrada"));
        sol.setAsunto(nuevaSolicitud.getAsunto());
        sol.setMensaje(nuevaSolicitud.getMensaje());
        sol.setFechaSolicitud(nuevaSolicitud.getFechaSolicitud());
        sol.setUsuario(nuevaSolicitud.getUsuario());
        sol.setEstado(nuevaSolicitud.getEstado());
        return solicitudSoporteRepository.save(sol);
    }

    public void eliminarSolicitudSoporte(Long idSolicitudSoporte) {
        solicitudSoporteRepository.deleteById(idSolicitudSoporte);
    }
}
