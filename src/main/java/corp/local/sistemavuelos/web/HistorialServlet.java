package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.HistorialDAO;
import corp.local.sistemavuelos.datos.ReservaDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int usuarioId = (Integer) session.getAttribute("usuarioId");
        String usuarioNombre = (String) session.getAttribute("usuarioNombre");
        
        System.out.println("=== CARGANDO HISTORIAL ===");
        System.out.println("Usuario ID: " + usuarioId);
        System.out.println("Usuario Nombre: " + usuarioNombre);
        
        try {
            // Obtener historial de búsquedas
            HistorialDAO historialDAO = new HistorialDAO();
            List<Object[]> historialBusquedas = historialDAO.obtenerHistorialBusquedas(usuarioId);
            
            // Obtener historial de reservas
            ReservaDAO reservaDAO = new ReservaDAO();
            List<Object[]> historialReservas = reservaDAO.obtenerReservasPorUsuario(usuarioId);
            
            // Log para depuración
            System.out.println("Reservas encontradas: " + (historialReservas != null ? historialReservas.size() : 0));
            System.out.println("Búsquedas encontradas: " + (historialBusquedas != null ? historialBusquedas.size() : 0));
            
            if (historialReservas != null && !historialReservas.isEmpty()) {
                for (Object[] reserva : historialReservas) {
                    System.out.println("Reserva en lista: " + reserva[0] + " - " + reserva[1] + " " + reserva[2]);
                }
            } else {
                System.out.println("No se encontraron reservas para el usuario ID: " + usuarioId);
            }
            
            request.setAttribute("historialBusquedas", historialBusquedas);
            request.setAttribute("historialReservas", historialReservas);
            
        } catch (Exception e) {
            System.out.println("ERROR en HistorialServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el historial: " + e.getMessage());
        }
        
        request.getRequestDispatcher("historial.jsp").forward(request, response);
    }
}