package com.Ecomarket.Usuarios.dto;

import lombok.Data;

@Data
public class UsuarioDevolucionDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String correo;
    private String dirUsuario;
}
