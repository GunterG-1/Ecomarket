package com.Ecomarket.Usuarios.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rol")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "usuarios")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column
    private String nombreRol; 

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "rolUsuario",
        joinColumns = @JoinColumn(name = "idRol"),
        inverseJoinColumns = @JoinColumn(name = "idUsuario")
    )
    private Set<Usuario> usuarios = new LinkedHashSet<>();
}