package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.model.Usuario;
import com.Ecomarket.Usuarios.repository.DevolucionRepository;
import com.Ecomarket.Usuarios.repository.ReclamacionRepository;
import com.Ecomarket.Usuarios.repository.SolicitudSoporteRepository;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import com.Ecomarket.Usuarios.client.DetalleVentaClient.DetalleVentaDTO;

import com.Ecomarket.Usuarios.client.VentaCliente;
import com.Ecomarket.Usuarios.client.VentaCliente.VentaDTO;
import com.Ecomarket.Usuarios.dto.DevolucionResponseDTO;
import com.Ecomarket.Usuarios.dto.ProductoDevolucionDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaCliente ventaClient;

   

    

   

    // --------- DEVOLUCION ---------
    public List<Devolucion> listarDevoluciones() {
        return devolucionRepository.findAll();
    }

    
    public DevolucionResponseDTO crearDevolucion(Devolucion devolucion) {
        // Obtener el nombre del producto desde el microservicio de productos
       VentaDTO venta = ventaClient.obtenerVenta(devolucion.getIdVenta());
    if (venta == null) 
        throw new RuntimeException("Venta no encontrada");
        Long idUsuario = venta.getIdUsuario();
    Usuario usuario = usuarioRepository.findById(idUsuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    devolucion.setUsuario(usuario);
        
    DetalleVentaDTO detalleSeleccionado = null;
    if (venta.getDetalles() != null) {
        for (DetalleVentaDTO detalle : venta.getDetalles()) {
            if (detalle.getIdProducto().equals(devolucion.getIdProducto())) {
                detalleSeleccionado = detalle;
                break;
            }
        }
    }
    if (detalleSeleccionado == null) throw new RuntimeException("Producto no encontrado en la venta");
    

    ProductoDevolucionDTO productoDTO = new ProductoDevolucionDTO();
    productoDTO.setIdProducto(detalleSeleccionado.getIdProducto());
    productoDTO.setNombreProducto(detalleSeleccionado.getNombreProducto());

    devolucion.setFechaDevolucion(new Date());
    devolucion.setIdProducto(detalleSeleccionado.getIdProducto());
    devolucion.setNombreProducto(detalleSeleccionado.getNombreProducto());
    devolucion.setDetalle("Devolución solicitada por el usuario");
    devolucion.setEstado("Pendiente");
    
    
    Devolucion saved = devolucionRepository.save(devolucion);

    DevolucionResponseDTO response = new DevolucionResponseDTO();
    response.setIdDevolucion(saved.getIdDevolucion());
    response.setIdVenta(saved.getIdVenta());
    response.setProducto(productoDTO);
    response.setCantidad(saved.getCantidad());
    response.setMotivo(saved.getMotivo());
    response.setFechaDevolucion(saved.getFechaDevolucion());
    response.setDetalle(saved.getDetalle());
    response.setEstado(saved.getEstado());
    

    return response;
}

    public Devolucion actualizarDevolucion(Long idDevolucion, Devolucion nuevaDevolucion) {
        Devolucion dev = devolucionRepository.findById(idDevolucion)
                .orElseThrow(() -> new RuntimeException("Devolución no encontrada"));
        dev.setMotivo(nuevaDevolucion.getMotivo());
        dev.setCantidad(nuevaDevolucion.getCantidad());
        return devolucionRepository.save(dev);
    }

    public void eliminarDevolucion(Long idDevolucion) {
        devolucionRepository.deleteById(idDevolucion);
    }

    // --------- RECLAMACION ---------
    public List<Reclamacion> listarReclamaciones() {
        return reclamacionRepository.findAll();
    }

    public Reclamacion crearReclamacion(Reclamacion reclamacion) {
       
        
        Usuario usuario = usuarioRepository.findById(reclamacion.getUsuario().getIdUsuario())
    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    reclamacion.setUsuario(usuario);
        reclamacion.setFechaReclamo(new Date());
        reclamacion.setEstado("Pendiente");
        return reclamacionRepository.save(reclamacion);
    }

    public Reclamacion actualizarReclamacion(Long idReclamacion, Reclamacion nuevaReclamacion) {
        Reclamacion rec = reclamacionRepository.findById(idReclamacion)
                .orElseThrow(() -> new RuntimeException("Reclamación no encontrada"));
        rec.setAsunto(nuevaReclamacion.getAsunto());
        rec.setMensaje(nuevaReclamacion.getMensaje());
        return reclamacionRepository.save(rec);
    }

    public void eliminarReclamacion(Long idReclamacion) {
        reclamacionRepository.deleteById(idReclamacion);
    }

    // --------- SOLICITUD SOPORTE ---------
    public List<SolicitudSoporte> listarSolicitudesSoporte() {
        return solicitudSoporteRepository.findAll();
    }


    public SolicitudSoporte crearSolicitudSoporte(SolicitudSoporte solicitud) {
          Usuario usuario = usuarioRepository.findById(solicitud.getUsuario().getIdUsuario())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    solicitud.setUsuario(usuario);

    solicitud.setFechaSolicitud(new Date()); 
    solicitud.setEstado("Pendiente"); 
        return solicitudSoporteRepository.save(solicitud);
    }

    public SolicitudSoporte actualizarSolicitudSoporte(Long idSolicitudSoporte, SolicitudSoporte nuevaSolicitud) {
        SolicitudSoporte sol = solicitudSoporteRepository.findById(idSolicitudSoporte)
                .orElseThrow(() -> new RuntimeException("Solicitud de soporte no encontrada"));
        sol.setAsunto(nuevaSolicitud.getAsunto());
        sol.setMensaje(nuevaSolicitud.getMensaje());
        return solicitudSoporteRepository.save(sol);
    }

    public void eliminarSolicitudSoporte(Long idSolicitudSoporte) {
        solicitudSoporteRepository.deleteById(idSolicitudSoporte);
    }
}
