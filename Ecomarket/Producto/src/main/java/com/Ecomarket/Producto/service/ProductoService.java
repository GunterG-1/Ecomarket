package com.Ecomarket.Producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    // Agregar producto (no permite códigos repetidos)
    public Producto agregarProducto(Producto producto){
        if (productoRepository.existsByCodigo(producto.getCodigo())){
            throw new RuntimeException("El código ingresado ya existe");
        }
        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> listarProductos(){
        return productoRepository.findAll();
    }

    // Buscar producto por ID
    public Optional<Producto> findById(Long idProducto){
        return productoRepository.findById(idProducto);
    }

    // Actualizar producto
    public Producto actualizarProducto(Long idProducto, Producto productoActualizado){
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setNombreProducto(productoActualizado.getNombreProducto());
        producto.setCodigo(productoActualizado.getCodigo());
        producto.setDescripcionProducto(productoActualizado.getDescripcionProducto());
        producto.setPrecioUnitario(productoActualizado.getPrecioUnitario());
        producto.setStock(productoActualizado.getStock());
        producto.setCategoria(productoActualizado.getCategoria());
        return productoRepository.save(producto);
    }

    

    // Actualizar stock restando la cantidad vendida
    public void actualizarStock(Long idProducto, int cantidadVendida) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() < cantidadVendida) {
            throw new RuntimeException("Stock insuficiente");
        }
        producto.setStock(producto.getStock() - cantidadVendida);
        productoRepository.save(producto);
    }


   
    // Eliminar producto
    public void eliminarProducto(Long idProducto) {
        productoRepository.deleteById(idProducto);
    }
}
