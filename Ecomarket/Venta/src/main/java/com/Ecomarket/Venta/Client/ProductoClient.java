package com.Ecomarket.Venta.Client;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductoClient {
    @Autowired
    private RestTemplate restTemplate;

    public ProductoDTO obtenerProductoPorId(Long idProducto) {
        String url = "http://localhost:8082/api/productos/" + idProducto;
        return restTemplate.getForObject(url, ProductoDTO.class);
    }
     public void actualizarStock(Long idProducto, int cantidadVendida) {
        String url = "http://localhost:8082/api/productos/" + idProducto + "/actualizarStock?cantidadVendida=" + cantidadVendida;
        restTemplate.put(url, null);
    }
    


    // DTO interno para recibir datos del microservicio Producto
    public static class ProductoDTO {
        private Long idProducto;
        private String nombreProducto;
        private BigDecimal precioUnitario;
        private int stock;

        public Long setIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }

}
