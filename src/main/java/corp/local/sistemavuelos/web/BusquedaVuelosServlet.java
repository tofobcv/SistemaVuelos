package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.ActualizarVueloDAO;
import corp.local.sistemavuelos.datos.HistorialDAO;
import corp.local.sistemavuelos.dominio.Vuelo;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/buscarVuelos")
public class BusquedaVuelosServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
         
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
         
        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaIda = request.getParameter("fechaIda");
        String fechaVuelta = request.getParameter("fechaVuelta");
        String pasajeros = request.getParameter("pasajeros");
        
        // Nuevos parámetros de filtro
        String aerolinea = request.getParameter("aerolinea");
        String precioMaximo = request.getParameter("precioMaximo");
        String escalas = request.getParameter("escalas");
        String horario = request.getParameter("horario");
        String ordenarPor = request.getParameter("ordenarPor");
         
        try {
            ActualizarVueloDAO vueloDao = new ActualizarVueloDAO();
            List<Vuelo> vuelos = vueloDao.buscarVuelos(origen, destino, fechaIda);
            
            // Aplicar filtros adicionales
            if (aerolinea != null && !aerolinea.isEmpty()) {
                vuelos.removeIf(v -> !v.getAerolinea().equalsIgnoreCase(aerolinea));
            }
            
            if (precioMaximo != null && !precioMaximo.isEmpty()) {
                double precioMax = Double.parseDouble(precioMaximo);
                vuelos.removeIf(v -> v.getPrecio() > precioMax);
            }
            
            if (escalas != null && !escalas.isEmpty()) {
                int maxEscalas = Integer.parseInt(escalas);
                vuelos.removeIf(v -> v.getEscalas() > maxEscalas);
            }
            
            if (horario != null && !horario.isEmpty()) {
                // Filtrar por horario (mañana, tarde, noche)
                vuelos = filtrarPorHorario(vuelos, horario);
            }
            
            // Ordenar resultados
            if (ordenarPor != null && !ordenarPor.isEmpty()) {
                vuelos = ordenarVuelos(vuelos, ordenarPor);
            }
             
            request.setAttribute("vuelos", vuelos);
            request.setAttribute("origen", origen);
            request.setAttribute("destino", destino);
            request.setAttribute("fechaIda", fechaIda);
            request.setAttribute("fechaVuelta", fechaVuelta);
            request.setAttribute("pasajeros", pasajeros);
            
            // Guardar parámetros de filtro para mostrar en la vista
            request.setAttribute("aerolineaFiltro", aerolinea);
            request.setAttribute("precioMaximoFiltro", precioMaximo);
            request.setAttribute("escalasFiltro", escalas);
            request.setAttribute("horarioFiltro", horario);
            request.setAttribute("ordenarPorFiltro", ordenarPor);
            
            // Guardar en historial si el usuario está logueado
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("usuarioId") != null) {
                int usuarioId = (Integer) session.getAttribute("usuarioId");
                HistorialDAO historialDAO = new HistorialDAO();
                historialDAO.guardarBusqueda(usuarioId, origen, destino, fechaIda, fechaVuelta, 
                                            Integer.parseInt(pasajeros));
            }
             
            request.getRequestDispatcher("resultados.jsp").forward(request, response);
             
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            request.getRequestDispatcher("resultados.jsp").forward(request, response);
        }
    }
    
    private List<Vuelo> filtrarPorHorario(List<Vuelo> vuelos, String horario) {
        switch (horario) {
            case "manana":
                vuelos.removeIf(v -> {
                    int hora = v.getFechaSalida().getHours();
                    return hora < 6 || hora >= 12;
                });
                break;
            case "tarde":
                vuelos.removeIf(v -> {
                    int hora = v.getFechaSalida().getHours();
                    return hora < 12 || hora >= 18;
                });
                break;
            case "noche":
                vuelos.removeIf(v -> {
                    int hora = v.getFechaSalida().getHours();
                    return hora < 18 || hora >= 24;
                });
                break;
        }
        return vuelos;
    }
    
    private List<Vuelo> ordenarVuelos(List<Vuelo> vuelos, String criterio) {
        switch (criterio) {
            case "precio":
                vuelos.sort((v1, v2) -> Double.compare(v1.getPrecio(), v2.getPrecio()));
                break;
            case "precio_desc":
                vuelos.sort((v1, v2) -> Double.compare(v2.getPrecio(), v1.getPrecio()));
                break;
            case "duracion":
                vuelos.sort((v1, v2) -> Integer.compare(v1.getDuracionMinutos(), v2.getDuracionMinutos()));
                break;
            case "salida":
                vuelos.sort((v1, v2) -> v1.getFechaSalida().compareTo(v2.getFechaSalida()));
                break;
        }
        return vuelos;
    }
}