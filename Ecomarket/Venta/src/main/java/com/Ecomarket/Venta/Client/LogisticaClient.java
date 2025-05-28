package com.Ecomarket.Venta.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LogisticaClient {
    @Autowired
    private RestTemplate restTemplate;

    public void crearEnvio(EnvioDTO envio) {
        String url = "http://localhost:8084/api/envios"; // Ajusta el puerto si es diferente
        restTemplate.postForObject(url, envio, Void.class);
    }

    public static class EnvioDTO {
        public String nombreUsuario;
        public String apellidoUsuario;
        public String correo;
        public String dirUsuario;
        public String nombreProducto;
        public Long idProducto;
       
    }
}
