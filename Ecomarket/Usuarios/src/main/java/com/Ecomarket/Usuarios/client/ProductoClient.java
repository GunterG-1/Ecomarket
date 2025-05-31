package com.Ecomarket.Usuarios.client;

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
        return restTemplate.getForObject(url, ProductoDTO.class);

    }
        public static class ProductoDTO {
        private Long idProducto;
        private String nombreProducto;
        

        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        }
    }