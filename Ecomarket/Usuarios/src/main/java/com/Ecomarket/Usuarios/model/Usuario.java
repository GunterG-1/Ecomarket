package com.Ecomarket.Usuarios.model;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = false, length = 25, nullable = false)
    private String nombreUsuario;

    @Column(unique = false, length = 25, nullable = false)
    private String apellidoUsuario; 

    @Column(unique = true, length = 50, nullable = false)
    private String correo;

    @Column(unique = false, length = 25, nullable = false)
    private String contrasena;

    @Column(length = 50, nullable = false) 
    private String dirUsuario;

    private String metodoPago;

    private boolean activo = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "usuarios")
    private Set<Rol> roles = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private List<Devolucion> devoluciones;

    @OneToMany(mappedBy = "usuario")
    private List<Reclamacion> reclamaciones;

    @OneToMany(mappedBy = "usuario")
    private List<SolicitudSoporte> solicitudesSoporte;

}