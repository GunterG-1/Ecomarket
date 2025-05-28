package com.Ecomarket.Venta.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cupon")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cupon {
    
    @Id
    @Column( length= 25, nullable = true)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal descuento;

    @Column(nullable = false)
    private Boolean activo;
    
    
    
}