package com.Ecomarket.Logistica;

import com.Ecomarket.Logistica.controller.EnvioController;
import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
public class LogisticaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarEnvios() throws Exception {
        List<Envio> envios = List.of(new Envio(), new Envio());
        when(envioService.listarEnvios()).thenReturn(envios);
        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerEnvio_Existente() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        when(envioService.obtenerEnvio(1L)).thenReturn(Optional.of(envio));
        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerEnvio_NoExistente() throws Exception {
        when(envioService.obtenerEnvio(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/envios/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearEnvio_Exitoso() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        when(envioService.crearEnvio(any(Envio.class))).thenReturn(envio);
        mockMvc.perform(post("/api/envios/crearEnvio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearEnvio_Error() throws Exception {
        when(envioService.crearEnvio(any(Envio.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(post("/api/envios/crearEnvio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Envio())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarEnvio_Exitoso() throws Exception {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        when(envioService.actualizarEnvio(eq(1L), any(Envio.class))).thenReturn(envio);
        mockMvc.perform(put("/api/envios/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envio)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarEnvio_NoEncontrado() throws Exception {
        when(envioService.actualizarEnvio(eq(2L), any(Envio.class))).thenThrow(new RuntimeException("No encontrado"));
        mockMvc.perform(put("/api/envios/actualizar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Envio())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarEnvio() throws Exception {
        doNothing().when(envioService).delete(1L);
        mockMvc.perform(delete("/api/envios/1"))
                .andExpect(status().isNoContent());
    }
}
