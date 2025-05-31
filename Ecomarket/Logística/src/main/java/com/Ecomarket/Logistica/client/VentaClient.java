package com.Ecomarket.Logistica.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    // DTO interno para recibir datos del microservicio Venta
    public static class VentaDTO {

        private String nombreUsuario;
        private String apellidoUsuario;
        private String correo;
        private String dirUsuario;
        private List<DetalleVentaDTO> detalles;

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getApellidoUsuario() { return apellidoUsuario; }
        public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getDirUsuario() { return dirUsuario; }
        public void setDirUsuario(String dirUsuario) { this.dirUsuario = dirUsuario; }


        public List<DetalleVentaDTO> getDetalles() {
            return detalles;
        }
        public void setDetalles(List<DetalleVentaDTO> detalles) {
            this.detalles = detalles;
        }

        // DTO interno para detalles
        public static class DetalleVentaDTO {
            private String nombreProducto;
            private Long idProducto;
            private Integer cantidad;

            // Getters y setters


            public Long getIdProducto(){
                return idProducto;
            } 
            public void setIdProducto(Long idProducto){
                this.idProducto = idProducto;
            }


            public String getNombreProducto() {
                return nombreProducto;
            }
            public void setNombreProducto(String nombreProducto) {
                this.nombreProducto = nombreProducto;
            }

            public Integer getCantidad() {
                return cantidad;
            }
            public void setCantidad(Integer cantidad) {
                this.cantidad = cantidad;
            }
        }
    }
}


