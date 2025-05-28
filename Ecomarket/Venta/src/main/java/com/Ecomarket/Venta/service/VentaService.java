package com.Ecomarket.Venta.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Ecomarket.Venta.Client.LogisticaClient;
import com.Ecomarket.Venta.Client.ProductoClient;
import com.Ecomarket.Venta.Client.UsuarioClient;
import com.Ecomarket.Venta.model.Cupon;
import com.Ecomarket.Venta.model.DetalleVenta;
import com.Ecomarket.Venta.model.Venta;
import com.Ecomarket.Venta.repository.CuponRepository;
import com.Ecomarket.Venta.repository.VentaRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private LogisticaClient logisticaClient;

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta registrarVenta(Venta venta, String codigo) {
        venta.setFechaVenta(LocalDate.now());

        // Validar y actualizar stock de cada producto
        venta.getDetalles().forEach(d -> {
            // 1. Obtener datos reales del producto
            ProductoClient.ProductoDTO producto = productoClient.obtenerProductoPorId(d.getIdProducto());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + d.getIdProducto());
            }
            if (producto.getStock() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }
            // 2. Usar el precio y nombre real del producto
            d.setPrecioUnitario(producto.getPrecioUnitario());
            d.setNombreProducto(producto.getNombreProducto());
            d.setTotal(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())));
            d.setVenta(venta);

            // 3. Actualizar stock en Producto
            productoClient.actualizarStock(d.getIdProducto(), d.getCantidad());
        });

        BigDecimal total = venta.getDetalles().stream()
            .map(DetalleVenta::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Optional<Cupon> cuponOpt = Optional.ofNullable(codigo)
            .map(String::trim)
            .flatMap(c -> cuponRepository.findById(codigo));

        if (cuponOpt.isPresent() && Boolean.TRUE.equals(cuponOpt.get().getActivo())) {
            BigDecimal descuento = cuponOpt.get().getDescuento();
            total = total.subtract(total.multiply(descuento));
            venta.setCupon(cuponOpt.get());
        }

        // Obtener datos reales del usuario
        UsuarioClient.UsuarioDTO usuario = usuarioClient.obtenerUsuarioPorId(venta.getIdUsuario());
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + venta.getIdUsuario());
        }
        venta.setNombreUsuario(usuario.getNombreUsuario());
        venta.setCorreo(usuario.getCorreo());

        venta.setTotal(total);
        Venta ventaGuardada = ventaRepository.save(venta);

        // Crear envíos en Logística
        for (DetalleVenta d : venta.getDetalles()) {
            LogisticaClient.EnvioDTO envio = new LogisticaClient.EnvioDTO();
            envio.nombreUsuario = venta.getNombreUsuario();
            envio.apellidoUsuario = ""; // Si tienes este dato en Usuario, agrégalo
            envio.correo = venta.getCorreo();
            envio.dirUsuario = d.getDirUsuario();
            envio.nombreProducto = d.getNombreProducto();
            envio.idProducto = d.getIdProducto();
            logisticaClient.crearEnvio(envio);
        }

        enviarFacturaPorCorreo(ventaGuardada);
        return ventaGuardada;
    }

    private void enviarFacturaPorCorreo(Venta venta) {
        try {
            StringBuilder cuerpo = new StringBuilder();
            cuerpo.append("Factura electrónica\n\n");
            cuerpo.append("Cliente: ").append(venta.getNombreUsuario()).append("\n");
            cuerpo.append("Fecha: ").append(venta.getFechaVenta()).append("\n");
            cuerpo.append("Total: $").append(venta.getTotal()).append("\n\n");
            cuerpo.append("Detalle:\n");
            for (DetalleVenta d : venta.getDetalles()) {
                cuerpo.append(" - ")
                      .append(d.getNombreProducto())
                      .append(" | Unitario: $").append(d.getPrecioUnitario())
                      .append(" x ").append(d.getCantidad())
                      .append(" = $").append(d.getTotal()) // Usar el campo total del detalle
                      .append("\n");
            }

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(venta.getCorreo());
            helper.setSubject("Factura electrónica - Venta " + venta.getIdVenta());
            helper.setText(cuerpo.toString(), false);

            mailSender.send(mensaje);

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }

    public List<Venta> buscarPorCorreo(String correo) {
        return ventaRepository.findByCorreo(correo);
    }

    public List<Venta> buscarPorNombreUsuario(String nombreUsuario) {
        return ventaRepository.findByNombreUsuario(nombreUsuario);
    }

    public Venta obtenerVentaPorId(Long idVenta) {
        return ventaRepository.findById(idVenta).orElse(null);
    }

}
