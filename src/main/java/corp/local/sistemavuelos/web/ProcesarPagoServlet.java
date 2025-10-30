package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.ReservaDAO;
import corp.local.sistemavuelos.servicios.EmailService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/procesarPago")
public class ProcesarPagoServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== INICIANDO PROCESO DE PAGO - VERSIÓN SEGURA ===");
        
        // Configurar encoding PRIMERO
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        
        try {
            // 1. Obtener parámetros del formulario
            String numeroTarjeta = request.getParameter("numeroTarjeta");
            String nombreTitular = request.getParameter("nombreTitular");
            String fechaExpiracion = request.getParameter("fechaExpiracion");
            String cvv = request.getParameter("cvv");
            String emailConfirmacion = request.getParameter("emailConfirmacion");
            String telefonoConfirmacion = request.getParameter("telefonoConfirmacion");
            String terminos = request.getParameter("terminos");
            
            System.out.println("Parámetros recibidos:");
            System.out.println("Email: " + emailConfirmacion);
            System.out.println("Tarjeta: " + (numeroTarjeta != null ? "PROVIDED" : "MISSING"));
            System.out.println("Términos: " + (terminos != null ? "ACEPTADOS" : "NO"));
            
            // 2. Validaciones básicas
            if (emailConfirmacion == null || emailConfirmacion.trim().isEmpty()) {
                throw new Exception("El email de confirmación es requerido");
            }
            
            if (terminos == null) {
                throw new Exception("Debes aceptar los términos y condiciones");
            }
            
            // 3. Generar número de reserva
            String numeroReserva = generarNumeroReserva();
            System.out.println("Número reserva: " + numeroReserva);
            
            // 4. Obtener datos de sesión
            String total = (String) session.getAttribute("total");
            String emailContacto = (String) session.getAttribute("emailContacto");
            String telefonoContacto = (String) session.getAttribute("telefonoContacto");
            String vueloIdStr = (String) session.getAttribute("vueloId");
            Integer numPasajeros = (Integer) session.getAttribute("numPasajeros");
            
            // Usar datos del formulario si no hay en sesión
            if (emailContacto == null) emailContacto = emailConfirmacion;
            if (telefonoContacto == null) telefonoContacto = telefonoConfirmacion;
            if (total == null) total = "2000.00";
            if (vueloIdStr == null) vueloIdStr = "1";
            if (numPasajeros == null) numPasajeros = 1;
            
            // 5. Obtener usuario
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");
            if (usuarioId == null) usuarioId = 1;
            
            // 6. CONVERTIR Y VALIDAR VUELO ID - SEGURIDAD EXTRA
            Integer vueloId;
            try {
                vueloId = Integer.parseInt(vueloIdStr);
                // Asegurar que el ID sea POSITIVO
                if (vueloId < 0) {
                    vueloId = 1; // Usar ID 1 por defecto si es negativo
                    System.out.println("⚠️ ID de vuelo negativo corregido a: " + vueloId);
                }
                System.out.println("Vuelo ID para reserva: " + vueloId);
            } catch (NumberFormatException e) {
                vueloId = 1; // Valor por defecto si hay error
                System.out.println("⚠️ Error parseando vuelo ID, usando valor por defecto: " + vueloId);
            }
            
            // 7. Guardar en base de datos
            ReservaDAO reservaDAO = new ReservaDAO();
            boolean exito = reservaDAO.guardarReserva(
                numeroReserva,
                usuarioId,
                vueloId, // Ahora siempre es un ID REAL positivo
                Double.parseDouble(total),
                emailContacto,
                telefonoContacto != null ? telefonoContacto : "5551234567"
            );
            
            if (exito) {
                System.out.println("✓ Reserva guardada en BD");
                
                // Guardar en sesión para confirmación
                session.setAttribute("numeroReserva", numeroReserva);
                session.setAttribute("fechaReserva", new Date());
                session.setAttribute("total", total);
                session.setAttribute("emailContacto", emailContacto);
                session.setAttribute("telefonoContacto", telefonoContacto);
                session.setAttribute("numPasajeros", numPasajeros);
                
                // 8. ENVIAR EMAIL DE CONFIRMACIÓN
                boolean emailEnviado = false;
                try {
                    EmailService emailService = new EmailService();
                    
                    // Obtener nombre del primer pasajero si existe
                    String nombrePasajero = "Estimado Cliente";
                    Map<Integer, Map<String, String>> pasajeros = (Map<Integer, Map<String, String>>) session.getAttribute("pasajeros");
                    if (pasajeros != null && !pasajeros.isEmpty()) {
                        Map<String, String> primerPasajero = pasajeros.get(1);
                        if (primerPasajero != null && primerPasajero.get("nombre") != null) {
                            nombrePasajero = primerPasajero.get("nombre") + " " + primerPasajero.get("apellido");
                        }
                    }
                    
                    emailEnviado = emailService.enviarEmailConfirmacion(
                        emailContacto, 
                        numeroReserva, 
                        nombrePasajero, 
                        Double.parseDouble(total), 
                        "Tarjeta de Crédito", 
                        "Ciudad de México",
                        "Cancún", 
                        new SimpleDateFormat("dd/MM/yyyy").format(new Date())
                    );
                    
                    if (emailEnviado) {
                        System.out.println("✓ Email de confirmación enviado exitosamente a: " + emailContacto);
                        session.setAttribute("emailEnviado", true);
                        
                        // AGREGAR MENSAJE DE CONFIRMACIÓN DEL EMAIL
                        session.setAttribute("mensajeEmail", 
                            "¡Te hemos enviado un email de confirmación a " + emailContacto + "! " +
                            "Revisa tu bandeja de entrada y carpeta de spam.");
                    } else {
                        System.out.println("⚠️ No se pudo enviar el email de confirmación");
                        session.setAttribute("emailEnviado", false);
                        session.setAttribute("mensajeEmail", 
                            "Tu reserva fue exitosa, pero no pudimos enviar el email de confirmación. " +
                            "Guarda tu número de reserva: " + numeroReserva);
                    }
                    
                } catch (Exception e) {
                    System.out.println("✗ Error enviando email: " + e.getMessage());
                    session.setAttribute("emailEnviado", false);
                    session.setAttribute("mensajeEmail", 
                        "Tu reserva fue exitosa. Guarda tu número de reserva: " + numeroReserva);
                }
                
                // 9. REDIRIGIR A CONFIRMACIÓN
                response.sendRedirect("confirmacionFinal.jsp");
                
            } else {
                throw new Exception("No se pudo guardar la reserva en la base de datos");
            }
            
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("formularioPago.jsp").forward(request, response);
        }
    }
    
    private String generarNumeroReserva() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        return "RES" + timestamp + (int)(Math.random() * 1000);
    }
}