package com.Ecomarket.Logistica;

import com.Ecomarket.Logistica.client.VentaClient;
import com.Ecomarket.Logistica.client.VentaClient.VentaDTO;
import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.repository.EnvioRepository;
import com.Ecomarket.Logistica.service.EnvioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LogisticaServiceTest {
    @Autowired
    private EnvioService envioService;

    @MockBean
    private EnvioRepository envioRepository;
    @MockBean
    private VentaClient ventaClient;

    @Test
    void testListarEnvios() {
        List<Envio> envios = List.of(new Envio(), new Envio());
        when(envioRepository.findAll()).thenReturn(envios);
        List<Envio> result = envioService.listarEnvios();
        assertEquals(2, result.size());
    }

    @Test
    void testObtenerEnvio_Existente() {
        Envio envio = new Envio();
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        Optional<Envio> result = envioService.obtenerEnvio(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testObtenerEnvio_NoExistente() {
        when(envioRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Envio> result = envioService.obtenerEnvio(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCrearEnvio_Exitoso() {
        Envio envio = new Envio();
        envio.setIdVenta(1L);
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setNombreUsuario("Juan");
        ventaDTO.setApellidoUsuario("Pérez");
        ventaDTO.setCorreo("juan@correo.com");
        ventaDTO.setDirUsuario("Calle 123");
        when(ventaClient.obtenerVenta(1L)).thenReturn(ventaDTO);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        Envio result = envioService.crearEnvio(envio);
        assertNotNull(result);
        assertEquals("Juan", result.getNombreUsuario());
        assertEquals("Pérez", result.getApellidoUsuario());
        assertEquals("juan@correo.com", result.getCorreo());
        assertEquals("Calle 123", result.getDestino());
        assertEquals("Pendiente", result.getEstado());
        assertNotNull(result.getFechaEnvio());
        assertNotNull(result.getFechaEntregaEstimada());
    }

    @Test
    void testCrearEnvio_VentaNoEncontrada() {
        Envio envio = new Envio();
        envio.setIdVenta(99L);
        when(ventaClient.obtenerVenta(99L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> envioService.crearEnvio(envio));
    }

    @Test
    void testActualizarEnvio_Existente() {
        Envio envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setEstado("Pendiente");
        Envio actualizado = new Envio();
        actualizado.setEstado("Enviado");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        Envio result = envioService.actualizarEnvio(1L, actualizado);
        assertEquals("Enviado", result.getEstado());
    }

    @Test
    void testActualizarEnvio_NoExistente() {
        Envio actualizado = new Envio();
        when(envioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> envioService.actualizarEnvio(2L, actualizado));
    }

    @Test
    void testDeleteEnvio() {
        doNothing().when(envioRepository).deleteById(1L);
        assertDoesNotThrow(() -> envioService.delete(1L));
    }
}
