package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.client.VentaCliente;
import com.Ecomarket.Usuarios.controller.Re_De_So_Controller;
import com.Ecomarket.Usuarios.dto.DevolucionResponseDTO;
import com.Ecomarket.Usuarios.model.Devolucion;
import com.Ecomarket.Usuarios.model.Reclamacion;
import com.Ecomarket.Usuarios.model.SolicitudSoporte;
import com.Ecomarket.Usuarios.repository.DevolucionRepository;
import com.Ecomarket.Usuarios.repository.ReclamacionRepository;
import com.Ecomarket.Usuarios.repository.RolRepository;
import com.Ecomarket.Usuarios.repository.SolicitudSoporteRepository;
import com.Ecomarket.Usuarios.repository.UsuarioRepository;
import com.Ecomarket.Usuarios.service.Re_De_So_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(Re_De_So_Controller.class)
public class Re_De_So_ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    private RolRepository rolRepository;
    @MockBean
    private VentaCliente ventaClient;

    @Autowired
    private ObjectMapper objectMapper;

    // --------- DEVOLUCIONES ---------
    @Test
    void testListarDevoluciones() throws Exception {
        List<Devolucion> devoluciones = List.of(new Devolucion(), new Devolucion());
        when(service.listarDevoluciones()).thenReturn(devoluciones);
        mockMvc.perform(get("/api/servicio/devoluciones"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearDevolucion() throws Exception {
        Devolucion devolucion = new Devolucion();
        DevolucionResponseDTO response = new DevolucionResponseDTO();
        when(service.crearDevolucion(any(Devolucion.class))).thenReturn(response);
        mockMvc.perform(post("/api/servicio/crear/devoluciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(devolucion)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarDevolucion() throws Exception {
        Devolucion nueva = new Devolucion();
        when(service.actualizarDevolucion(eq(1L), any(Devolucion.class))).thenReturn(nueva);
        mockMvc.perform(put("/api/servicio/devo/actulaizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarDevolucion() throws Exception {
        doNothing().when(service).eliminarDevolucion(1L);
        mockMvc.perform(delete("/api/servicio/devoluciones/1"))
                .andExpect(status().isNoContent());
    }

    // --------- RECLAMACIONES ---------
    @Test
    void testListarReclamaciones() throws Exception {
        List<Reclamacion> reclamaciones = List.of(new Reclamacion(), new Reclamacion());
        when(service.listarReclamaciones()).thenReturn(reclamaciones);
        mockMvc.perform(get("/api/servicio/reclamaciones"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearReclamacion() throws Exception {
        Reclamacion reclamacion = new Reclamacion();
        when(service.crearReclamacion(any(Reclamacion.class))).thenReturn(reclamacion);
        mockMvc.perform(post("/api/servicio/crear/reclamaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reclamacion)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarReclamacion() throws Exception {
        Reclamacion nueva = new Reclamacion();
        when(service.actualizarReclamacion(eq(1L), any(Reclamacion.class))).thenReturn(nueva);
        mockMvc.perform(put("/api/servicio/recla/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarReclamacion() throws Exception {
        doNothing().when(service).eliminarReclamacion(1L);
        mockMvc.perform(delete("/api/servicio/reclamaciones/1"))
                .andExpect(status().isNoContent());
    }

    // --------- SOLICITUDES DE SOPORTE ---------
    @Test
    void testListarSolicitudesSoporte() throws Exception {
        List<SolicitudSoporte> solicitudes = List.of(new SolicitudSoporte(), new SolicitudSoporte());
        when(service.listarSolicitudesSoporte()).thenReturn(solicitudes);
        mockMvc.perform(get("/api/servicio/soporte"))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearSolicitudSoporte() throws Exception {
        SolicitudSoporte solicitud = new SolicitudSoporte();
        when(service.crearSolicitudSoporte(any(SolicitudSoporte.class))).thenReturn(solicitud);
        mockMvc.perform(post("/api/servicio/crear/soporte")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarSolicitudSoporte() throws Exception {
        SolicitudSoporte nueva = new SolicitudSoporte();
        when(service.actualizarSolicitudSoporte(eq(1L), any(SolicitudSoporte.class))).thenReturn(nueva);
        mockMvc.perform(put("/api/servicio/soporte/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarSolicitudSoporte() throws Exception {
        doNothing().when(service).eliminarSolicitudSoporte(1L);
        mockMvc.perform(delete("/api/servicio/soporte/1"))
                .andExpect(status().isNoContent());
    }
}
