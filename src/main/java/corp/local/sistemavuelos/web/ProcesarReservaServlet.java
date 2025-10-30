package corp.local.sistemavuelos.web;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/procesarReserva")
public class ProcesarReservaServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== PROCESANDO DATOS DE PASAJEROS ===");
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        System.out.println("Sesión ID: " + session.getId());
        
        // Recuperar datos críticos de la sesión primero
        String vueloId = (String) session.getAttribute("vueloId");
        String total = (String) session.getAttribute("total");
        
        System.out.println("Datos de sesión recuperados:");
        System.out.println("  - Vuelo ID: " + vueloId);
        System.out.println("  - Total: " + total);
        
        String emailContacto = request.getParameter("emailContacto");
        String telefonoContacto = request.getParameter("telefonoContacto");
        
        System.out.println("Datos del formulario:");
        System.out.println("  - Email: " + emailContacto);
        System.out.println("  - Teléfono: " + telefonoContacto);
        
        try {
            // Procesar datos de pasajeros
            Map<Integer, Map<String, String>> pasajeros = new HashMap<>();
            
            int numPasajeros = 0;
            for (int i = 1; i <= 4; i++) {
                String nombre = request.getParameter("nombre" + i);
                if (nombre != null && !nombre.trim().isEmpty()) {
                    numPasajeros++;
                    Map<String, String> pasajero = new HashMap<>();
                    pasajero.put("nombre", nombre);
                    pasajero.put("apellido", request.getParameter("apellido" + i));
                    pasajero.put("fechaNacimiento", request.getParameter("fechaNacimiento" + i));
                    pasajero.put("genero", request.getParameter("genero" + i));
                    pasajero.put("tipoDocumento", request.getParameter("tipoDocumento" + i));
                    pasajero.put("numeroDocumento", request.getParameter("numeroDocumento" + i));
                    pasajero.put("email", request.getParameter("email" + i));
                    pasajero.put("telefono", request.getParameter("telefono" + i));
                    pasajero.put("requerimientos", request.getParameter("requerimientos" + i));
                    
                    pasajeros.put(i, pasajero);
                    System.out.println("Pasajero " + i + ": " + nombre + " " + pasajero.get("apellido"));
                }
            }
            
            // GUARDAR TODOS LOS DATOS EN SESIÓN (IMPORTANTE PARA EL PAGO)
            session.setAttribute("emailContacto", emailContacto);
            session.setAttribute("telefonoContacto", telefonoContacto);
            session.setAttribute("pasajeros", pasajeros);
            session.setAttribute("numPasajeros", numPasajeros);
            
            // Asegurarse de que vueloId y total siguen en sesión
            if (vueloId != null) session.setAttribute("vueloId", vueloId);
            if (total != null) session.setAttribute("total", total);
            
            System.out.println("✓ TODOS LOS DATOS GUARDADOS EN SESIÓN:");
            System.out.println("  - Vuelo ID: " + session.getAttribute("vueloId"));
            System.out.println("  - Total: " + session.getAttribute("total"));
            System.out.println("  - Email: " + session.getAttribute("emailContacto"));
            System.out.println("  - Teléfono: " + session.getAttribute("telefonoContacto"));
            System.out.println("  - Pasajeros: " + session.getAttribute("numPasajeros"));
            
            // Verificar datos del vuelo en sesión
            Object vueloObj = session.getAttribute("vuelo");
            if (vueloObj != null) {
                System.out.println("  - Vuelo en sesión: SÍ");
            } else {
                System.out.println("  - Vuelo en sesión: NO");
            }
            
            System.out.println("Redirigiendo a formularioPago.jsp");
            
            // Redirigir al formulario de pago
            response.sendRedirect("formularioPago.jsp");
            
        } catch (Exception e) {
            System.out.println("✗ ERROR en ProcesarReservaServlet:");
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar los datos: " + e.getMessage());
            request.getRequestDispatcher("formularioPasajeros.jsp").forward(request, response);
        }
    }
}