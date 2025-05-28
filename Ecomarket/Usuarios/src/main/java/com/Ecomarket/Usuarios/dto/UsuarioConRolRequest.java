package com.Ecomarket.Usuarios.dto;

import lombok.Data;

@Data
public class UsuarioConRolRequest {
    private String nombreUsuario;
    private String apellidoUsuario;
    private String correo;
    private String contrasena;
    private String dirUsuario;
    private String metodoPago;
    private String nombreRol; // Aquí va el nombre del rol
}