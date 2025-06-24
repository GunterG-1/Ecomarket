package com.Ecomarket.Venta;

import com.Ecomarket.Venta.client.ProductoClient;
import com.Ecomarket.Venta.client.UsuarioClient;
import com.Ecomarket.Venta.client.ProductoClient.ProductoDTO;
import com.Ecomarket.Venta.client.UsuarioClient.UsuarioDTO;
import com.Ecomarket.Venta.model.DetalleVenta;
import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.repository.VentaRepository;
import com.Ecomarket.Venta.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VentaServiceTest {
    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;
    @MockBean
    private ProductoClient productoClient;
    @MockBean
    private UsuarioClient usuarioClient;

    @Test
    void testListarVentas() {
        List<Venta> ventas = List.of(new Venta(), new Venta());
        when(ventaRepository.findAll()).thenReturn(ventas);
        List<Venta> result = ventaService.listarVentas();
        assertEquals(2, result.size());
    }

    @Test
    void testRegistrarVenta_Exitoso() {
        Venta venta = new Venta();
        venta.setIdUsuario(1L);
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdProducto(10L);
        detalle.setCantidad(2);
        venta.setDetalle(List.of(detalle));

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombreUsuario("Juan");
        usuarioDTO.setApellidoUsuario("Pérez");
        usuarioDTO.setCorreo("juan@correo.com");
        usuarioDTO.setDirUsuario("Calle 123");
        when(usuarioClient.obtenerPorId(1L)).thenReturn(usuarioDTO);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombreProducto("Producto1");
        productoDTO.setPrecioUnitario(java.math.BigDecimal.valueOf(100.0));
        when(productoClient.obtenerProducto(10L)).thenReturn(productoDTO);

        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        doNothing().when(productoClient).actualizarStock(anyLong(), anyInt());

        Venta result = ventaService.registrarVenta(venta);
        assertNotNull(result);
        assertEquals("Juan", result.getNombreUsuario());
        assertEquals("Producto1", result.getDetalle().get(0).getNombreProducto());
    }

    @Test
    void testRegistrarVenta_ProductoNoEncontrado() {
        Venta venta = new Venta();
        venta.setIdUsuario(1L);
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdProducto(99L);
        detalle.setCantidad(1);
        venta.setDetalle(List.of(detalle));
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        when(usuarioClient.obtenerPorId(1L)).thenReturn(usuarioDTO);
        when(productoClient.obtenerProducto(99L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> ventaService.registrarVenta(venta));
    }

    @Test
    void testFindById_Existente() {
        Venta venta = new Venta();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        Optional<Venta> result = ventaService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindById_NoExistente() {
        when(ventaRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Venta> result = ventaService.findById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testActualizarVenta_Existente() {
        Venta venta = new Venta();
        venta.setDetalle(new ArrayList<>());
        DetalleVenta detalleExistente = new DetalleVenta();
        detalleExistente.setIdProducto(1L);
        detalleExistente.setCantidad(2);
        venta.getDetalle().add(detalleExistente);

        Venta ventaActualizada = new Venta();
        ventaActualizada.setDetalle(new ArrayList<>());
        DetalleVenta detalleNuevo = new DetalleVenta();
        detalleNuevo.setIdProducto(1L);
        detalleNuevo.setCantidad(3);
        ventaActualizada.getDetalle().add(detalleNuevo);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        doNothing().when(productoClient).actualizarStock(anyLong(), anyInt());

        Venta result = ventaService.actualizarVenta(1L, ventaActualizada);
        assertNotNull(result);
        verify(productoClient, times(2)).actualizarStock(anyLong(), anyInt());
    }

    @Test
    void testActualizarVenta_NoExistente() {
        Venta ventaActualizada = new Venta();
        when(ventaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> ventaService.actualizarVenta(2L, ventaActualizada));
    }

    @Test
    void testDeleteVenta() {
        doNothing().when(ventaRepository).deleteById(1L);
        assertDoesNotThrow(() -> ventaService.delete(1L));
    }
}
