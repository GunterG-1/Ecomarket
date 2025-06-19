package com.Ecomarket.Logistica;

import com.Ecomarket.Logistica.model.Envio;
import com.Ecomarket.Logistica.repository.EnvioRepository;
import com.Ecomarket.Logistica.client.VentaClient;
import com.Ecomarket.Logistica.client.VentaClient.VentaDTO;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private VentaClient ventaClient;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        VentaDTO[] ventas = ventaClient.obtenerTodasLasVentas();
        if(ventas == null)
            return;
        for (VentaDTO venta : ventas) {
            if (venta == null) continue;

            Envio envio = new Envio();
            envio.setOrigen(faker.address().cityName());
            envio.setNombreUsuario(venta.getNombreUsuario());
            envio.setApellidoUsuario(venta.getApellidoUsuario());
            envio.setCorreo(venta.getCorreo());
            envio.setEstado(faker.options().option("Pendiente", "En tránsito", "Entregado", "Cancelado"));
            envio.setIdVenta(venta.getIdVenta());
            envio.setDestino(venta.getDirUsuario());
            envio.setFechaEnvio(LocalDate.now().minusDays(faker.number().numberBetween(0, 5)));
            envio.setFechaEntregaEstimada(LocalDate.now().plusDays(faker.number().numberBetween(1, 10)));
            envio.setResumenPedido(faker.lorem().sentence(12));
            envioRepository.save(envio);
        }
         // Imprimir datos generados
        System.out.println("=== ENVIOS GENERADOS ===");
        for (Envio envio : envioRepository.findAll()) {
            System.out.println(
                "ID: " + envio.getIdEnvio() +
                " | VentaID: " + envio.getIdVenta() +
                " | Usuario: " + envio.getNombreUsuario() + " " + envio.getApellidoUsuario() +
                " | Correo: " + envio.getCorreo() +
                " | Estado: " + envio.getEstado() +
                " | Origen: " + envio.getOrigen() +
                " | Destino: " + envio.getDestino() +
                " | Fecha Envío: " + envio.getFechaEnvio() +
                " | Fecha Entrega Estimada: " + envio.getFechaEntregaEstimada() +
                " | Resumen Pedido: " + envio.getResumenPedido()
            );
    }
}
}
