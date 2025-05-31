package com.Ecomarket.Venta.model;



import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table (name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @Column(unique=true, length= 25 , nullable = false) 
    private String nombreUsuario;

    @Column(length = 25,nullable = false)
    private String apellidoUsuario;
    
    @Column(nullable = false)
    private String dirUsuario;

    private Long idUsuario;

    @Column( nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVenta;

    @Column(nullable = false)
    private String correo;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<DetalleVenta> detalle;
}