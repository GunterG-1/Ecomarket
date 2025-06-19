package com.Ecomarket.Usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "devolucion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDevolucion;
    
    private Long idVenta;

    private Long idProducto;
    
    private String nombreProducto; 
    
    private int cantidad;
   
    @Column(length = 200, nullable = false)
    private String motivo;

    @Column(nullable = true)
    private Date fechaDevolucion;

    private String detalle;
    
    private String estado; // Ej: "Pendiente", "Procesada"

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @JsonIgnore
    private Usuario usuario;
}