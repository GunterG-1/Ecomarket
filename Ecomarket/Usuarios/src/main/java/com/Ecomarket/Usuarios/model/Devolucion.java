package com.Ecomarket.Usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "devolucion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDevolucion;

    @Column(unique = true, length = 200, nullable = false)
    private String motivo;

    private Long idProducto;

    private String nombreProducto; // Nuevo campo para el nombre del producto

    @Column(nullable = true)
    @Temporal(TemporalType.DATE) // Solo guarda la fecha, sin hora
    private Date fechaDevolucion;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    private String estado; // Ej: "Pendiente", "Procesada"
}