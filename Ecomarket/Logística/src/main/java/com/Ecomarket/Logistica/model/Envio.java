package com.Ecomarket.Logistica.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table (name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Envio {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idEnvio;

@Column(length = 100, nullable = false)
private String origen;

@Column(length = 25, nullable = false)
private String nombreUsuario;

@Column(length = 25, nullable = false)
private String apellidoUsuario;

@Column(length = 50, nullable = false, unique = true)
private String correo;

@Column(length = 20, nullable = false)
private String estado; 

private Long idVenta;

@Column(length = 100, nullable = false)
private String destino;

@Column(nullable = true)
private LocalDate fechaEnvio;

@Column(nullable = true)
private LocalDate fechaEntregaEstimada;

@Column(length = 300)
private String ResumenPedido;
}