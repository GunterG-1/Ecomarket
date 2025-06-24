package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.model.*;
import com.Ecomarket.Usuarios.repository.*;
import com.Ecomarket.Usuarios.service.Re_De_So_service;
import com.Ecomarket.Usuarios.client.VentaCliente;
import com.Ecomarket.Usuarios.client.DetalleVentaClient.DetalleVentaDTO;
import com.Ecomarket.Usuarios.client.VentaCliente.VentaDTO;
import com.Ecomarket.Usuarios.dto.DevolucionResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class Re_De_So_ServiceTest {
    @Autowired
    private Re_De_So_service service;

    @MockBean
    private DevolucionRepository devolucionRepository;
    @MockBean
    private ReclamacionRepository reclamacionRepository;
    @MockBean
    private SolicitudSoporteRepository solicitudSoporteRepository;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private VentaCliente ventaClient;

    // --------- DEVOLUCION ---------
    @Test
    void testListarDevoluciones() {
        List<Devolucion> devoluciones = List.of(new Devolucion(), new Devolucion());
        when(devolucionRepository.findAll()).thenReturn(devoluciones);
        List<Devolucion> resultado = service.listarDevoluciones();
        assertEquals(2, resultado.size());
    }

    @Test
    void testCrearDevolucion_Exitoso() {
        Devolucion devolucion = new Devolucion();
        devolucion.setIdVenta(1L);
        devolucion.setIdProducto(10L);
        devolucion.setCantidad(1);
        VentaDTO venta = new VentaDTO();
        venta.setIdUsuario(2L);
        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setIdProducto(10L);
        detalle.setNombreProducto("ProductoTest");
        // Corrección: asignar detalles directamente si el campo es público, o usa el setter si existe
        venta.setDetalle(List.of(detalle)); // Usar el setter correcto
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2L);
        when(ventaClient.obtenerVenta(1L)).thenReturn(venta);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(devolucionRepository.save(any(Devolucion.class))).thenAnswer(i -> {
            Devolucion d = i.getArgument(0);
            d.setIdDevolucion(100L);
            return d;
        });
        DevolucionResponseDTO response = service.crearDevolucion(devolucion);
        assertNotNull(response);
        assertEquals(100L, response.getIdDevolucion());
        assertEquals("ProductoTest", response.getProducto().getNombreProducto());
    }

    @Test
    void testActualizarDevolucion_Existente() {
        Devolucion existente = new Devolucion();
        existente.setIdDevolucion(1L);
        Devolucion nueva = new Devolucion();
        nueva.setMotivo("Nuevo motivo");
        nueva.setCantidad(2);
        when(devolucionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(devolucionRepository.save(any(Devolucion.class))).thenReturn(nueva);
        Devolucion result = service.actualizarDevolucion(1L, nueva);
        assertEquals("Nuevo motivo", result.getMotivo());
        assertEquals(2, result.getCantidad());
    }

    @Test
    void testEliminarDevolucion() {
        doNothing().when(devolucionRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.eliminarDevolucion(1L));
    }

    // --------- RECLAMACION ---------
    @Test
    void testListarReclamaciones() {
        List<Reclamacion> reclamaciones = List.of(new Reclamacion(), new Reclamacion());
        when(reclamacionRepository.findAll()).thenReturn(reclamaciones);
        List<Reclamacion> resultado = service.listarReclamaciones();
        assertEquals(2, resultado.size());
    }

    @Test
    void testCrearReclamacion_Exitoso() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Reclamacion reclamacion = new Reclamacion();
        reclamacion.setUsuario(usuario);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reclamacionRepository.save(any(Reclamacion.class))).thenReturn(reclamacion);
        Reclamacion result = service.crearReclamacion(reclamacion);
        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals("Pendiente", result.getEstado());
    }

    @Test
    void testActualizarReclamacion_Existente() {
        Reclamacion existente = new Reclamacion();
        existente.setIdReclamacion(1L);
        Reclamacion nueva = new Reclamacion();
        nueva.setAsunto("Nuevo asunto");
        nueva.setMensaje("Nuevo mensaje");
        when(reclamacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reclamacionRepository.save(any(Reclamacion.class))).thenReturn(nueva);
        Reclamacion result = service.actualizarReclamacion(1L, nueva);
        assertEquals("Nuevo asunto", result.getAsunto());
        assertEquals("Nuevo mensaje", result.getMensaje());
    }

    @Test
    void testEliminarReclamacion() {
        doNothing().when(reclamacionRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.eliminarReclamacion(1L));
    }

    // --------- SOLICITUD SOPORTE ---------
    @Test
    void testListarSolicitudesSoporte() {
        List<SolicitudSoporte> solicitudes = List.of(new SolicitudSoporte(), new SolicitudSoporte());
        when(solicitudSoporteRepository.findAll()).thenReturn(solicitudes);
        List<SolicitudSoporte> resultado = service.listarSolicitudesSoporte();
        assertEquals(2, resultado.size());
    }

    @Test
    void testCrearSolicitudSoporte_Exitoso() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        SolicitudSoporte solicitud = new SolicitudSoporte();
        solicitud.setUsuario(usuario);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(solicitudSoporteRepository.save(any(SolicitudSoporte.class))).thenReturn(solicitud);
        SolicitudSoporte result = service.crearSolicitudSoporte(solicitud);
        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals("Pendiente", result.getEstado());
    }

    @Test
    void testActualizarSolicitudSoporte_Existente() {
        SolicitudSoporte existente = new SolicitudSoporte();
        existente.setIdSolicitud(1L);
        SolicitudSoporte nueva = new SolicitudSoporte();
        nueva.setAsunto("Nuevo asunto");
        nueva.setMensaje("Nuevo mensaje");
        when(solicitudSoporteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(solicitudSoporteRepository.save(any(SolicitudSoporte.class))).thenReturn(nueva);
        SolicitudSoporte result = service.actualizarSolicitudSoporte(1L, nueva);
        assertEquals("Nuevo asunto", result.getAsunto());
        assertEquals("Nuevo mensaje", result.getMensaje());
    }

    @Test
    void testEliminarSolicitudSoporte() {
        doNothing().when(solicitudSoporteRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.eliminarSolicitudSoporte(1L));
    }
}
