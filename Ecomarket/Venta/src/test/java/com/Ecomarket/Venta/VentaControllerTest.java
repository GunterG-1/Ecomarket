package com.Ecomarket.Venta;

import com.Ecomarket.Venta.controller.VentaController;
import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.service.VentaService;
import com.Ecomarket.Venta.repository.VentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;
    @MockBean
    private VentaRepository ventaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarVentas_ConContenido() throws Exception {
        List<Venta> ventas = List.of(new Venta(), new Venta());
        when(ventaService.listarVentas()).thenReturn(ventas);
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarVentas_SinContenido() throws Exception {
        when(ventaService.listarVentas()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRegistrarVenta_Exitoso() throws Exception {
        Venta venta = new Venta();
        venta.setIdVenta(1L);
        when(ventaService.registrarVenta(any(Venta.class))).thenReturn(venta);
        mockMvc.perform(post("/api/ventas/nueva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistrarVenta_Error() throws Exception {
        when(ventaService.registrarVenta(any(Venta.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(post("/api/ventas/nueva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Venta())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerVenta_Existente() throws Exception {
        Venta venta = new Venta();
        venta.setIdVenta(1L);
        when(ventaService.findById(1L)).thenReturn(Optional.of(venta));
        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerVenta_NoExistente() throws Exception {
        when(ventaService.findById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/ventas/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarVenta_Exitoso() throws Exception {
        Venta venta = new Venta();
        venta.setIdVenta(1L);
        when(ventaService.actualizarVenta(eq(1L), any(Venta.class))).thenReturn(venta);
        mockMvc.perform(put("/api/ventas/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarVenta_NoEncontrado() throws Exception {
        when(ventaService.actualizarVenta(eq(2L), any(Venta.class))).thenThrow(new RuntimeException("No encontrado"));
        mockMvc.perform(put("/api/ventas/actualizar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Venta())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarVenta_Existente() throws Exception {
        when(ventaService.findById(1L)).thenReturn(Optional.of(new Venta()));
        doNothing().when(ventaService).delete(1L);
        mockMvc.perform(delete("/api/ventas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarVenta_NoExistente() throws Exception {
        when(ventaService.findById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/ventas/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerVentasPorUsuario() throws Exception {
        List<Venta> ventas = List.of(new Venta(), new Venta());
        when(ventaRepository.findByIdUsuario(1L)).thenReturn(ventas);
        mockMvc.perform(get("/api/ventas/usuario/1"))
                .andExpect(status().isOk());
    }
@Test
void testRegistrarVentaPerformanceMultiple() throws Exception {
    Venta venta = new Venta();
    when(ventaService.registrarVenta(any(Venta.class))).thenReturn(venta);

    String ventaJson = new ObjectMapper().writeValueAsString(venta);
    StringBuilder resultados = new StringBuilder();

    for (int i = 1; i <= 100; i++) {
        long start = System.currentTimeMillis();
        mockMvc.perform(post("/api/ventas/nueva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ventaJson))
                .andExpect(status().isOk());
        long end = System.currentTimeMillis();
        resultados.append("Iteración ").append(i)
                  .append(": ").append(end - start).append(" ms\n");
    }
    try (java.io.FileWriter fw = new java.io.FileWriter("venta-registrar-performance-test.log", true)) {
        fw.write(resultados.toString());
    }
}
}

