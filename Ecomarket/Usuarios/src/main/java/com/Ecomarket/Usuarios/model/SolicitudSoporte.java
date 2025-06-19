package com.Ecomarket.Usuarios.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "solicitudes_soporte")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    @Column(length = 200,nullable = false)
    private String asunto;
    
    @Column(length = 300,nullable = false)
    private String mensaje;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE) // Solo la fecha, sin hora
    private Date fechaSolicitud; // Fecha y hora de la solicitud;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @JsonBackReference
    private Usuario usuario;

    private String estado; // Ej: "Enviado", "Atendido"
}