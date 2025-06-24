package com.Ecomarket.Producto;

import com.Ecomarket.Producto.controller.ProductoController;
import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarProductos() throws Exception {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoService.listarProductos()).thenReturn(productos);
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerProducto_Existente() throws Exception {
        Producto producto = new Producto();
        producto.setIdProducto(1L);
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerProducto_NoExistente() throws Exception {
        when(productoService.findById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/productos/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAgregarProducto_Exitoso() throws Exception {
        Producto producto = new Producto();
        producto.setIdProducto(1L);
        when(productoService.agregarProducto(any(Producto.class))).thenReturn(producto);
        mockMvc.perform(post("/api/productos/crearProducto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAgregarProducto_Error() throws Exception {
        when(productoService.agregarProducto(any(Producto.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(post("/api/productos/crearProducto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Producto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarStock_Exitoso() throws Exception {
        doNothing().when(productoService).actualizarStock(eq(1L), eq(5));
        mockMvc.perform(put("/api/productos/1/actualizarStock?cantidadVendida=5"))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarStock_Error() throws Exception {
        doThrow(new RuntimeException("Stock insuficiente")).when(productoService).actualizarStock(eq(1L), eq(10));
        mockMvc.perform(put("/api/productos/1/actualizarStock?cantidadVendida=10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarProducto_Exitoso() throws Exception {
        Producto producto = new Producto();
        producto.setIdProducto(1L);
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(producto);
        mockMvc.perform(put("/api/productos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarProducto_NoEncontrado() throws Exception {
        when(productoService.actualizarProducto(eq(2L), any(Producto.class))).thenThrow(new RuntimeException("No encontrado"));
        mockMvc.perform(put("/api/productos/actualizar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Producto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProducto() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
   @Test
void testListarProductoPerformanceMultiple() throws Exception {
    List<Producto> productos = List.of(new Producto(), new Producto());
    when(productoService.listarProductos()).thenReturn(productos);

    StringBuilder resultados = new StringBuilder();
    for (int i = 1; i <= 100; i++) {
        long start = System.currentTimeMillis();
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
        long end = System.currentTimeMillis();
        resultados.append("Iteración ").append(i)
                  .append(": ").append(end - start).append(" ms\n");
    }
    try (java.io.FileWriter fw = new java.io.FileWriter("performance-test.log", true)) {
        fw.write(resultados.toString());
    }
}
}



