package com.Ecomarket.Venta.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {
    @Autowired
    private RestTemplate restTemplate;

    public UsuarioDTO obtenerUsuarioPorId(Long idUsuario) {
        String url = "http://localhost:8081/api/usuarios/" + idUsuario;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }

    // DTO interno para recibir datos del microservicio Usuario
    public static class UsuarioDTO {
        private Long idUsuario;
        private String nombreUsuario;
        private String correo;

        public Long getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }
}