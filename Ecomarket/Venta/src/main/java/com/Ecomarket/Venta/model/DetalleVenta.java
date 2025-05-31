package com.Ecomarket.Venta.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private Long idProducto;

    @Column( length = 100, nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column( precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @ManyToOne
    @JoinColumn(name = "idVenta")
    @JsonBackReference
    private Venta venta;

    @JsonProperty("total")
    public BigDecimal getTotal() {
        if (precioUnitario!= null && cantidad != null) {
            return precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        }
        return BigDecimal.ZERO;
    }

}