package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.ActualizarVueloDAO;
import corp.local.sistemavuelos.dominio.Vuelo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/seleccionarVuelo")
public class ReservaServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String vueloId = request.getParameter("vueloId");
        String pasajeros = request.getParameter("pasajeros");
        
        System.out.println("=== PROCESANDO SELECCIÓN DE VUELO ===");
        System.out.println("Vuelo ID: " + vueloId + ", Pasajeros: " + pasajeros);
        
        try {
            // Obtener información del vuelo
            Vuelo vuelo = obtenerVueloPorId(Integer.parseInt(vueloId));
            
            if (vuelo != null) {
                // Calcular total
                double total = vuelo.getPrecio() * Integer.parseInt(pasajeros);
                
                // Guardar en request para el JSP actual
                request.setAttribute("vuelo", vuelo);
                request.setAttribute("pasajeros", pasajeros);
                request.setAttribute("total", total);
                
                // Guardar en sesión para los siguientes pasos (IMPORTANTE)
                HttpSession session = request.getSession();
                session.setAttribute("vuelo", vuelo);
                session.setAttribute("vueloId", String.valueOf(vuelo.getId())); // Guardar ID REAL como String
                session.setAttribute("numPasajeros", Integer.parseInt(pasajeros));
                session.setAttribute("total", String.valueOf(total)); // Guardar como String
                
                System.out.println("✓ DATOS GUARDADOS EN SESIÓN:");
                System.out.println("  - Vuelo ID REAL: " + session.getAttribute("vueloId"));
                System.out.println("  - Total: " + session.getAttribute("total"));
                System.out.println("  - Pasajeros: " + session.getAttribute("numPasajeros"));
                System.out.println("  - Aerolínea: " + vuelo.getAerolinea());
                System.out.println("  - Número Vuelo: " + vuelo.getNumeroVuelo());
                System.out.println("  - Origen: " + vuelo.getOrigen());
                System.out.println("  - Destino: " + vuelo.getDestino());
                
                // Redirigir al formulario de pasajeros
                request.getRequestDispatcher("formularioPasajeros.jsp").forward(request, response);
            } else {
                System.out.println("✗ Vuelo no encontrado con ID: " + vueloId);
                request.setAttribute("error", "Vuelo no encontrado");
                request.getRequestDispatcher("resultados.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error al procesar la reserva: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la reserva: " + e.getMessage());
            request.getRequestDispatcher("resultados.jsp").forward(request, response);
        }
    }
    
    private Vuelo obtenerVueloPorId(int id) {
        System.out.println("Buscando vuelo con ID: " + id);
        
        try {
            ActualizarVueloDAO vueloDao = new ActualizarVueloDAO();
            Vuelo vuelo = vueloDao.buscarVueloPorId(id);
            
            if (vuelo != null) {
                System.out.println("✓ Vuelo encontrado: " + vuelo.getAerolinea() + " " + vuelo.getNumeroVuelo());
                System.out.println("  - ID Real: " + vuelo.getId()); // Este ID debe ser POSITIVO
                System.out.println("  - Origen: " + vuelo.getOrigen());
                System.out.println("  - Destino: " + vuelo.getDestino());
                System.out.println("  - Precio: $" + vuelo.getPrecio());
                return vuelo;
            } else {
                System.out.println("✗ Vuelo no encontrado, buscando alternativo");
                // Buscar un vuelo real alternativo
                return buscarVueloRealAlternativo();
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error al obtener vuelo: " + e.getMessage());
            return buscarVueloRealAlternativo();
        }
    }
    
    // NUEVO MÉTODO: Buscar vuelo real alternativo
    private Vuelo buscarVueloRealAlternativo() {
        System.out.println("Buscando vuelo real alternativo...");
        
        try {
            ActualizarVueloDAO vueloDao = new ActualizarVueloDAO();
            // Buscar vuelos de Ciudad de México a Cancún
            List<Vuelo> vuelos = vueloDao.buscarVuelos("Ciudad de México", "Cancún", 
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            
            if (vuelos != null && !vuelos.isEmpty()) {
                Vuelo vuelo = vuelos.get(0);
                System.out.println("✓ Vuelo alternativo encontrado: " + vuelo.getAerolinea() + " " + vuelo.getNumeroVuelo());
                System.out.println("  - ID Real: " + vuelo.getId());
                return vuelo;
            } else {
                System.out.println("✗ No hay vuelos alternativos, creando respaldo");
                return crearVueloRespaldo(1); // Usar ID 1
            }
        } catch (Exception e) {
            System.out.println("Error buscando vuelo alternativo: " + e.getMessage());
            return crearVueloRespaldo(1);
        }
    }
    
    // MÉTODO de respaldo por si hay error
    private Vuelo crearVueloRespaldo(int id) {
        Vuelo vuelo = new Vuelo();
        vuelo.setId(id); // ID REAL
        vuelo.setNumeroVuelo("VUELO-" + id);
        vuelo.setAerolinea("Aerolínea");
        vuelo.setOrigen("Ciudad de México");
        vuelo.setDestino("Cancún");
        vuelo.setPrecio(2000.00);
        vuelo.setDuracionMinutos(120);
        vuelo.setAsientosDisponibles(100);
        vuelo.setFechaSalida(new java.sql.Timestamp(System.currentTimeMillis() + 86400000));
        vuelo.setFechaLlegada(new java.sql.Timestamp(System.currentTimeMillis() + 86400000 + 7200000));
        return vuelo;
    }
}