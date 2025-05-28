package com.Ecomarket.Usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.Ecomarket.Usuarios.model.Rol;
import com.Ecomarket.Usuarios.repository.RolRepository;

@SpringBootApplication
public class UsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles(@Autowired RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                rolRepository.save(new Rol(null, "ADMINISTRADOR", null));
                rolRepository.save(new Rol(null, "CLIENTE", null));
                rolRepository.save(new Rol(null, "EMPLEADO VENTA", null));
                rolRepository.save(new Rol(null, "EMPLEADO LOGISTICA", null));
            }
        };
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
