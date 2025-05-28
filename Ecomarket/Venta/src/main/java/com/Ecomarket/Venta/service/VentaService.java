package com.Ecomarket.Venta.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;




import com.Ecomarket.Venta.model.Venta;

import com.Ecomarket.Venta.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

   

    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta) {
        Venta nuevaVenta = venta;
        return ventaRepository.save(nuevaVenta);
        
    }
    
   

    
    public Optional<Venta> obtenerVenta(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    // Comunicación con microservicio Producto a través del gateway
  
}

