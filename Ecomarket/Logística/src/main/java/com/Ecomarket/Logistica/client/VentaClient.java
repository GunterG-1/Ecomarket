package com.Ecomarket.Logistica.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.Ecomarket.Logistica.client.DetalleVentaClient.DetalleVentaDTO;


@Component
public class VentaClient {

    @Autowired
    private RestTemplate restTemplate;

    public VentaDTO obtenerVenta(Long idVenta) {
        String url = "http://localhost:8083/api/ventas/{idVenta}";
        Map<String, Long> params = new HashMap<>();
        params.put("idVenta", idVenta);
        return restTemplate.getForObject(url, VentaDTO.class,params);
    }
    public VentaDTO[] obtenerTodasLasVentas() {
        String url = "http://localhost:8083/api/ventas"; // Ajusta el puerto/ruta si es necesario
        return restTemplate.getForObject(url, VentaDTO[].class);
    }

    // DTO interno para recibir datos del microservicio Venta
    public static class VentaDTO {
        private Long idVenta;
        private String nombreUsuario;
        private String apellidoUsuario;
        private String correo;
        private String dirUsuario;

        private List<DetalleVentaDTO> detalle;

        public Long getIdVenta() { return idVenta; }
        public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }
        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getApellidoUsuario() { return apellidoUsuario; }
        public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getDirUsuario() { return dirUsuario; }
        public void setDirUsuario(String dirUsuario) { this.dirUsuario = dirUsuario; }

        public List<DetalleVentaDTO> getDetalles() {
            return detalle;
        }
        public void setDetalle(List<DetalleVentaDTO> detalle) {
            this.detalle = detalle;
        }

}
}

