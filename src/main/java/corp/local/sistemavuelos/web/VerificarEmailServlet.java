package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.UsuarioDAO;
import corp.local.sistemavuelos.dominio.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/verificarEmail")
public class VerificarEmailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        System.out.println("=== PROCESANDO VERIFICACIÓN DE EMAIL ===");
        System.out.println("Token recibido: " + token);
        
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Token de verificación inválido");
            System.out.println("ERROR: Token vacío");
            request.getRequestDispatcher("verificacion.jsp").forward(request, response);
            return;
        }
        
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDAO.ResultSetToken tokenInfo = usuarioDAO.validarToken(token);
            
            if (tokenInfo == null || !"verificacion".equals(tokenInfo.getTipo())) {
                request.setAttribute("error", "Token inválido o expirado");
                System.out.println("ERROR: Token inválido o expirado");
                request.getRequestDispatcher("verificacion.jsp").forward(request, response);
                return;
            }
            
            // Verificar email
            if (usuarioDAO.verificarEmail(tokenInfo.getUsuarioId())) {
                usuarioDAO.marcarTokenComoUsado(token);
                
                // Obtener información del usuario para mostrar
                Usuario usuario = usuarioDAO.buscarPorId(tokenInfo.getUsuarioId());
                request.setAttribute("usuarioNombre", usuario.getNombre());
                request.setAttribute("usuarioEmail", usuario.getEmail());
                
                request.setAttribute("mensaje", "¡Email verificado exitosamente! Ya puedes iniciar sesión.");
                System.out.println("✓ Email verificado para usuario ID: " + tokenInfo.getUsuarioId());
            } else {
                request.setAttribute("error", "Error al verificar el email");
                System.out.println("ERROR: No se pudo verificar email en BD");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            System.out.println("ERROR en servidor: " + e.getMessage());
        }
        
        request.getRequestDispatcher("verificacion.jsp").forward(request, response);
    }
}