package com.Ecomarket.Venta.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    private Long idUsuario;

    @Column(unique=true, length= 25 , nullable = false) 
    private String nombreUsuario;

    @Column( nullable = true)
    private LocalDate fechaVenta;


    @Column(nullable = false)
    private String correo;
    
    @ManyToOne
    @JoinColumn(name ="codigo")
    private Cupon cupon;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    @Column(precision = 38, scale = 2)
    private BigDecimal total;
}