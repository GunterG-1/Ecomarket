package com.Ecomarket.Logistica.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecomarket.Logistica.client.VentaClient;
import com.Ecomarket.Logistica.client.DetalleVentaClient.DetalleVentaDTO;
import com.Ecomarket.Logistica.client.VentaClient.VentaDTO;
import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.repository.EnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private VentaClient ventaClient;

    public List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }

    public Optional<Envio> obtenerEnvio(Long idEnvio) {
        return envioRepository.findById(idEnvio);
    }
    public Envio crearEnvio(Envio envio) {
    VentaDTO venta = ventaClient.obtenerVenta(envio.getIdVenta());
     if (venta == null) {
        throw new IllegalArgumentException("No se encontró la venta con ID: " + envio.getIdVenta());
    }
    envio.setNombreUsuario(venta.getNombreUsuario());
    envio.setApellidoUsuario(venta.getApellidoUsuario());
    envio.setCorreo(venta.getCorreo());
    envio.setDestino(venta.getDirUsuario());
    envio.setEstado("Pendiente");
    envio.setFechaEnvio(LocalDate.now());
    envio.setFechaEntregaEstimada(LocalDate.now().plusDays(3)); 

    List<DetalleVentaDTO> detalle =venta.getDetalles();
    if (detalle!= null && !detalle.isEmpty()) {
        String resumen = detalle.stream()
            .filter(Objects::nonNull)
            .map(d -> {
        Long id = d.getIdProducto() != null ? d.getIdProducto() : 0L;
        String nombre = d.getNombreProducto() != null ? d.getNombreProducto() : "Sin nombre";
        Integer cantidad = d.getCantidad() != null ? d.getCantidad() : 0;
        return "ID:" + id + " - " + nombre + " x " + cantidad;
             }) .collect(Collectors.joining(", "));
        envio.setResumenPedido(resumen.isEmpty()? "sin producto":resumen);
    } else {
        envio.setResumenPedido("Sin productos");
    } 

    return envioRepository.save(envio);
}

   
   public Envio actualizarEnvio(Long idEnvio, Envio envioActualizado) {
    Envio envio = envioRepository.findById(idEnvio)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

    // Actualiza solo los campos que vienen en envioActualizado (puedes agregar más si lo necesitas)
    if (envioActualizado.getOrigen() != null) {
        envio.setOrigen(envioActualizado.getOrigen());
    }
    if (envioActualizado.getEstado() != null) {
        envio.setEstado(envioActualizado.getEstado());
    }
    if (envioActualizado.getFechaEntregaEstimada() != null) {
        envio.setFechaEntregaEstimada(envioActualizado.getFechaEntregaEstimada());
    }
    
    

    return envioRepository.save(envio);
}

    public Envio save(Envio envio) {
        envio.setFechaEnvio(LocalDate.now());
        envio.setFechaEntregaEstimada(LocalDate.now().plusDays(3));
        return envioRepository.save(envio);
    }

    public void delete(Long id) {
        envioRepository.deleteById(id);
    }
    
}
