package com.Ecomarket.Usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "reclamacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reclamacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReclamos;

    @Column(length = 100, nullable = false)
    private String asunto;

    @Column(length = 200, nullable = false)
    private String mensaje;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE) // Solo guarda la fecha, sin hora
    private Date fechaReclamo;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    private String estado;
}