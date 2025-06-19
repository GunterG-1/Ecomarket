package com.Ecomarket.Usuarios;

import com.Ecomarket.Usuarios.client.DetalleVentaClient;
import com.Ecomarket.Usuarios.client.VentaCliente;
import com.Ecomarket.Usuarios.model.*;
import com.Ecomarket.Usuarios.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private DevolucionRepository devolucionRepository;
    @Autowired
    private ReclamacionRepository reclamacionRepository;
    @Autowired
    private SolicitudSoporteRepository solicitudSoporteRepository;
    @Autowired
    private VentaCliente ventaCliente;
    

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Obtener roles existentes
        List<Rol> roles = rolRepository.findAll();

        // Crear usuarios
        List<Usuario> usuariosCreados = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(faker.name().firstName());
            usuario.setApellidoUsuario(faker.name().lastName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setContrasena(faker.internet().password());
            String direccion = faker.address().fullAddress();
            usuario.setDirUsuario(direccion.length() > 100 ? direccion.substring(0, 100) : direccion);
            usuario.setMetodoPago(faker.business().creditCardType());
            usuario.setActivo(faker.bool().bool());
            usuario.setRoles(new HashSet<>());
            usuario.getRoles().add(roles.get(random.nextInt(roles.size())));
            usuarioRepository.save(usuario);
            usuariosCreados.add(usuario);
        }

        // Crear devoluciones SOLO para usuarios con ventas existentes
        for (Usuario usuario : usuariosCreados) {
            List<VentaCliente.VentaDTO> ventas = ventaCliente.obtenerVentasPorUsuario(usuario.getIdUsuario());
            if (ventas == null || ventas.isEmpty()) continue;

            for (VentaCliente.VentaDTO venta : ventas) {
                if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) continue;
                DetalleVentaClient.DetalleVentaDTO detalle = venta.getDetalles().get(0);

                Devolucion devolucion = new Devolucion();
                devolucion.setIdVenta(venta.getIdVenta());
                devolucion.setIdProducto(detalle.getIdProducto());
                devolucion.setNombreProducto(detalle.getNombreProducto());
                devolucion.setCantidad(faker.number().numberBetween(1, detalle.getCantidad() + 1));
                devolucion.setMotivo(faker.lorem().sentence());
                devolucion.setFechaDevolucion(new Date());
                devolucion.setEstado(faker.options().option("Pendiente", "Procesada"));
                devolucion.setUsuario(usuario);
                devolucionRepository.save(devolucion);
            }
        }

        // Crear reclamaciones
        List<Reclamacion> reclamacionesCreadas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Reclamacion reclamacion = new Reclamacion();
            Usuario usuario = usuariosCreados.get(random.nextInt(usuariosCreados.size()));
            reclamacion.setUsuario(usuario);
            reclamacion.setAsunto(faker.lorem().sentence());
            String mensaje = faker.lorem().paragraph();
            reclamacion.setMensaje(mensaje.length() > 255 ? mensaje.substring(0, 255) : mensaje);
            reclamacion.setFechaReclamo(new Date());
            reclamacion.setEstado(faker.options().option("Abierta", "Cerrada"));
            reclamacionRepository.save(reclamacion);
            reclamacionesCreadas.add(reclamacion);
        }

        // Crear solicitudes de soporte
        List<SolicitudSoporte> soportesCreados = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SolicitudSoporte soporte = new SolicitudSoporte();
            Usuario usuario = usuariosCreados.get(random.nextInt(usuariosCreados.size()));
            soporte.setUsuario(usuario);
            soporte.setAsunto(faker.lorem().sentence());
            String mensaje = faker.lorem().paragraph();
            soporte.setMensaje(mensaje.length() > 255 ? mensaje.substring(0, 255) : mensaje);
            soporte.setFechaSolicitud(new Date());
            soporte.setEstado(faker.options().option("Enviado", "Atendido"));
            solicitudSoporteRepository.save(soporte);
            soportesCreados.add(soporte);
        }

        // Imprimir datos generados para usar en otros microservicios
        System.out.println("=== USUARIOS GENERADOS ===");
        for (Usuario u : usuariosCreados) {
            System.out.println(
                "ID: " + u.getIdUsuario() +
                " | Nombre: " + u.getNombreUsuario() +
                " | Apellido: " + u.getApellidoUsuario() +
                " | Correo: " + u.getCorreo() +
                " | Dirección: " + u.getDirUsuario()
            );
        }

        System.out.println("=== DEVOLUCIONES GENERADAS ===");
        for (Usuario u : usuariosCreados) {
            System.out.println(
                "ID: " + u.getIdUsuario() +
                " | Nombre: " + u.getNombreUsuario() +
                " | Apellido: " + u.getApellidoUsuario() +
                " | Correo: " + u.getCorreo() +
                " | Devoluciones: "
            );
            if (u.getDevoluciones() != null) {
                for (Devolucion d : u.getDevoluciones()) {
                    System.out.println(
                        "   - idDevolucion: " + d.getIdDevolucion() +
                        ", idVenta: " + d.getIdVenta() +
                        ", idProducto: " + d.getIdProducto() +
                        ", nombreProducto: " + d.getNombreProducto() +
                        ", cantidad: " + d.getCantidad() +
                        ", motivo: " + d.getMotivo() +
                        ", estado: " + d.getEstado()
                    );
                }
            }
        }

        System.out.println("\n=== RECLAMACIONES GENERADAS ===");
        for (Reclamacion r : reclamacionesCreadas) {
            System.out.println(
                "ID: " + r.getIdReclamacion() +
                " | Asunto: " + r.getAsunto() +
                " | Mensaje: " + r.getMensaje() +
                " | Estado: " + r.getEstado()
            );
        }

        System.out.println("\n=== SOLICITUDES DE SOPORTE GENERADAS ===");
        for (SolicitudSoporte s : soportesCreados) {
            System.out.println(
                "ID: " + s.getIdSolicitud() +
                " | Asunto: " + s.getAsunto() +
                " | Mensaje: " + s.getMensaje() +
                " | Estado: " + s.getEstado()
            );
        }

        System.out.println("\n=== ROLES EXISTENTES ===");
        for (Rol rol : roles) {
            System.out.println("ID: " + rol.getIdRol() + " | Nombre Rol: " + rol.getNombreRol());
        }
    }
}

