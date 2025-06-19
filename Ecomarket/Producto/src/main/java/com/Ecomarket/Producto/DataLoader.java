package com.Ecomarket.Producto;

import com.Ecomarket.Producto.model.Producto;
import com.Ecomarket.Producto.repository.ProductoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        List<Producto> productosCreados = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Producto producto = new Producto();
            producto.setNombreProducto(faker.commerce().productName());
            producto.setCodigo(faker.code().ean8());
            producto.setDescripcionProducto(faker.lorem().sentence(10));
            producto.setPrecioUnitario(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 10000)));
            producto.setStock(faker.number().numberBetween(1, 500));
            producto.setCategoria(faker.commerce().department());
            productoRepository.save(producto);
            productosCreados.add(producto);
        }
        System.out.println("=== PRODUCTOS GENERADOS ===");
        for (Producto p : productosCreados) {
            System.out.println(
                "ID: " + p.getIdProducto() +
                " | Nombre: " + p.getNombreProducto() +
                " | Código: " + p.getCodigo() +
                " | Descripción: " + p.getDescripcionProducto() +
                " | Precio: " + p.getPrecioUnitario() +
                " | Stock: " + p.getStock() +
                " | Categoría: " + p.getCategoria()
            );
    }
}
}
