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

import java.io.File;
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
            if (!roles.isEmpty()) {
        usuario.getRoles().add(roles.get(random.nextInt(roles.size())));}
            usuarioRepository.save(usuario);
            usuariosCreados.add(usuario);
        }

        File flagFile = new File("devoluciones-creadas.flag");
    if (!flagFile.exists()) {
        // Primera vez: solo crea el archivo, no generes devoluciones
        flagFile.createNewFile();
        System.out.println("Primera ejecución: NO se crean devoluciones.");
    } else {
        // Segunda vez (o más): ejecuta la lógica de devoluciones
        System.out.println("Reinicio detectado: SE CREAN devoluciones.");

        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        // Obtener usuarios con al menos una venta
        List<Usuario> usuariosConVentas = new ArrayList<>();
        for (Usuario usuario : listaUsuarios) {
            List<VentaCliente.VentaDTO> ventas = ventaCliente.obtenerVentasPorUsuario(usuario.getIdUsuario());
            if (ventas != null && !ventas.isEmpty()) {
                usuariosConVentas.add(usuario);
            }
        }

        // Selecciona aleatoriamente usuarios para devoluciones
         int cantidadDevoluciones = 3; // Cambia este valor según lo que necesites
        Collections.shuffle(usuariosConVentas);
        List<Usuario> usuariosSeleccionados = usuariosConVentas.subList(0, Math.min(cantidadDevoluciones, usuariosConVentas.size()));

        for (Usuario usuario : usuariosSeleccionados) {
            try {
                List<VentaCliente.VentaDTO> ventas = ventaCliente.obtenerVentasPorUsuario(usuario.getIdUsuario());
                if (ventas == null || ventas.isEmpty()) continue;

                // Puedes elegir una venta aleatoria o la primera
                VentaCliente.VentaDTO venta = ventas.get(0);
                if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) continue;
                DetalleVentaClient.DetalleVentaDTO detalle = venta.getDetalles().get(0);

                boolean existe = devolucionRepository.existsByUsuarioAndIdVentaAndIdProducto(
                    usuario, venta.getIdVenta(), detalle.getIdProducto()
                );
                if (existe) continue;

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

            } catch (Exception e) {
                System.err.println("Error al procesar devoluciones para el usuario ID: " + usuario.getIdUsuario() + " - " + e.getMessage());
            }
        }

        // Crear reclamaciones
        List<Reclamacion> reclamacionesCreadas = new ArrayList<>();
        List<Usuario> usuariosDisponibles = usuarioRepository.findAll();
        if (!usuariosDisponibles.isEmpty()) {
        for (int i = 0; i < 3; i++) {
            Reclamacion reclamacion = new Reclamacion();
            Usuario usuario = usuariosDisponibles.get(random.nextInt(usuariosCreados.size()));
            reclamacion.setUsuario(usuario);
            reclamacion.setAsunto(faker.lorem().sentence());
            String mensaje = faker.lorem().paragraph();
            reclamacion.setMensaje(mensaje.length() > 255 ? mensaje.substring(0, 255) : mensaje);
            reclamacion.setFechaReclamo(new Date());
            reclamacion.setEstado(faker.options().option("Abierta", "Cerrada"));
            reclamacionRepository.save(reclamacion);
            reclamacionesCreadas.add(reclamacion);
        }
        } else {
            System.out.println("No hay usuarios disponibles para crear reclamaciones.");
        }

        // Crear solicitudes de soporte
        List<SolicitudSoporte> soportesCreados = new ArrayList<>();
         if (!usuariosDisponibles.isEmpty()) {
        for (int i = 0; i < 3; i++) {
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
        } else {
            System.out.println("No hay usuarios disponibles para crear solicitudes de soporte.");
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
}


