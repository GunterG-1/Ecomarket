package com.Ecomarket.Venta.model;

import java.math.BigDecimal;


import jakarta.persistence.*;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVenta;


    @Column(nullable = false, length = 100)
    private String nombreProducto; // nombre al momento de la venta
    @Column(nullable = false)
    
    private Long idProducto; // id del producto al momento de la venta

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario; // precio al momento de la venta

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 100)
    private String dirUsuario; // dirección del usuario al momento de la venta

    @ManyToOne
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total; // total por producto (precioUnitario * cantidad)

}