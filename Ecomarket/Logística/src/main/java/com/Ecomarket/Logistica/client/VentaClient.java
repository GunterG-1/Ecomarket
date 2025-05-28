package com.Ecomarket.Logistica.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VentaClient {

    @Autowired
    private RestTemplate restTemplate;

    public VentaDTO obtenerVentaPorId(Long idVenta) {
        String url = "http://localhost:808/api/ventas/" + idVenta;
        return restTemplate.getForObject(url, VentaDTO.class);
    }

    // DTO interno para recibir datos del microservicio Venta
    public static class VentaDTO {
        private String nombreUsuario;
        private String apellidoUsuario;
        private String correo;
        private String dirUsuario;
        private String nombreProducto;

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getApellidoUsuario() { return apellidoUsuario; }
        public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getDirUsuario() { return dirUsuario; }
        public void setDirUsuario(String dirUsuario) { this.dirUsuario = dirUsuario; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    }
}
