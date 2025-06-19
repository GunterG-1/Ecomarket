package com.Ecomarket.Usuarios.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DevolucionResponseDTO {
    private Long idDevolucion;
    private Long idVenta;
    
    private ProductoDevolucionDTO producto;
    private int cantidad;
    private String motivo;
    private Date fechaDevolucion;
    private String detalle;
    private String estado;
    
}
