package com.Ecomarket.Venta.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductoClient {
    @Autowired
    private RestTemplate restTemplate;

    public ProductoDTO obtenerProducto(Long idProducto) {
        String url = "http://localhost:8082/api/productos/{idProducto}";
        Map<String, Long> params = new HashMap<>();
        params.put("idProducto", idProducto);
        return restTemplate.getForObject(url, ProductoDTO.class, params);
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

        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }

}