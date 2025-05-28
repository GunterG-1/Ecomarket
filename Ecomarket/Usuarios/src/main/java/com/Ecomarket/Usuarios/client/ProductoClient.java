package com.Ecomarket.Usuarios.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductoClient {

    @Autowired
    private RestTemplate restTemplate;

    public ProductoDTO obtenerProductoPorId(Long idProducto) {
        String url = "http://localhost:8082/productos/" + idProducto;
        return restTemplate.getForObject(url, ProductoDTO.class);
    }
 public class ProductoDTO {
    private Long idProducto;
    private String nombreProducto;
    
    // Getters y setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}   
}
