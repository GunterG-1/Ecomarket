package com.Ecomarket.Venta.model;

import java.math.BigDecimal;


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
    private Long id;

    @Column(unique=true, length= 25 , nullable = false) 
    private String nom_usuario;

    @Column( nullable = true)
    private String fechaVenta;

    @Column ( nullable = true)
    private BigDecimal total;

    @Column(nullable = false)
    private String email;
    
    @ManyToOne
    @JoinColumn(name ="codigo")
    private Cupon cupon;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "venta_id") // Foreign key en detalle_venta
    private java.util.List<DetalleVenta> detalle;
}