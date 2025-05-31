package com.Ecomarket.Venta.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {

    @Autowired
    private RestTemplate restTemplate;

    public UsuarioDTO obtenerPorId(Long idUsuario) {
        String url = "http://localhost:8081/api/usuarios/{idUsuario}" ;
        Map<String, Long> params = new HashMap<>();
        params.put("idUsuario", idUsuario);
        return restTemplate.getForObject(url, UsuarioDTO.class, params);
    }

    // DTO interno para recibir datos del microservicio Usuario
    public static class UsuarioDTO {
        private Long idUsuario;
        private String nombreUsuario;
        private String apellidoUsuario;
        private String correo;
        private String dirUsuario;

        public Long getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getApellidoUsuario() { return apellidoUsuario; }
        public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }

        public String getCorreo(){return correo;}
        public void setCorreo(String correo){this.correo = correo;}

        public String getDirUsuario() { return dirUsuario; }
        public void setDirUsuario(String dirUsuario) { this.dirUsuario = dirUsuario; }
    }
}