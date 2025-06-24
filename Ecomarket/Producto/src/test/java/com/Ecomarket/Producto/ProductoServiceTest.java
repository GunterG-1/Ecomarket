package com.Ecomarket.Producto;

import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.repository.ProductoRepository;
import com.Ecomarket.Producto.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductoServiceTest {
    @Autowired
    private ProductoService productoService;
    
    @MockBean
    private ProductoRepository productoRepository;

    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarProducto_Exitoso() {
        Producto producto = new Producto();
        producto.setCodigo("ABC123");
        when(productoRepository.existsByCodigo("ABC123")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        Producto result = productoService.agregarProducto(producto);
        assertNotNull(result);
        assertEquals("ABC123", result.getCodigo());
    }

    @Test
    void testAgregarProducto_CodigoRepetido() {
        Producto producto = new Producto();
        producto.setCodigo("DUPLICADO");
        when(productoRepository.existsByCodigo("DUPLICADO")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> productoService.agregarProducto(producto));
    }

    @Test
    void testListarProductos() {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoRepository.findAll()).thenReturn(productos);
        List<Producto> result = productoService.listarProductos();
        assertEquals(2, result.size());
    }

    @Test
    void testFindById_Existente() {
        Producto producto = new Producto();
        producto.setCodigo("EXISTE");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Optional<Producto> result = productoService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("EXISTE", result.get().getCodigo());
    }

    @Test
    void testFindById_NoExistente() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Producto> result = productoService.findById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testActualizarProducto_Existente() {
        Producto existente = new Producto();
        existente.setIdProducto(1L);
        Producto actualizado = new Producto();
        actualizado.setNombreProducto("Nuevo");
        actualizado.setCodigo("COD123");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenReturn(actualizado);
        Producto result = productoService.actualizarProducto(1L, actualizado);
        assertEquals("Nuevo", result.getNombreProducto());
        assertEquals("COD123", result.getCodigo());
    }

    @Test
    void testActualizarProducto_NoExistente() {
        Producto actualizado = new Producto();
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productoService.actualizarProducto(2L, actualizado));
    }

    @Test
    void testActualizarStock_Exitoso() {
        Producto producto = new Producto();
        producto.setIdProducto(1L);
        producto.setStock(10);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        assertDoesNotThrow(() -> productoService.actualizarStock(1L, 5));
        assertEquals(5, producto.getStock());
    }

    @Test
    void testActualizarStock_Insuficiente() {
        Producto producto = new Producto();
        producto.setIdProducto(1L);
        producto.setStock(3);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        assertThrows(RuntimeException.class, () -> productoService.actualizarStock(1L, 5));
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);
        assertDoesNotThrow(() -> productoService.eliminarProducto(1L));
    }
    
}
    


