package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.UsuarioDAO;
import corp.local.sistemavuelos.dominio.Usuario;
import corp.local.sistemavuelos.servicios.EmailService;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/recuperacionPassword")
public class RecuperacionPasswordServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String paso = request.getParameter("paso");
        
        if (token != null && !token.trim().isEmpty()) {
            // Paso 2: Restablecer contraseña con token
            request.setAttribute("token", token);
            request.setAttribute("paso", "restablecer");
            request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
        } else {
            // Paso 1: Solicitar recuperación
            request.setAttribute("paso", "solicitar");
            request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String paso = request.getParameter("paso");
        
        if ("solicitar".equals(paso)) {
            solicitarRecuperacion(request, response);
        } else if ("restablecer".equals(paso)) {
            restablecerPassword(request, response);
        }
    }
    
    private void solicitarRecuperacion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            
            if (usuario != null) {
                // Generar token de recuperación
                String token = usuarioDAO.generarToken(usuario.getId(), "recuperacion");
                
                if (token != null) {
                    // Enviar email de recuperación
                    EmailService emailService = new EmailService();
                    String recoveryLink = request.getRequestURL().toString() + "?token=" + token;
                    
                    // Simular envío de email
                    System.out.println("Enlace de recuperación: " + recoveryLink);
                    
                    // Para prueba, podrías enviar un email real aquí
                    emailService.enviarEmailRecuperacion(email, recoveryLink, usuario.getNombre());
                }
            }
            
            // Por seguridad, siempre mostrar el mismo mensaje
            request.setAttribute("mensaje", "Si el email existe, recibirás instrucciones para restablecer tu contraseña.");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
        }
        
        request.setAttribute("paso", "solicitar");
        request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
    }
    
    private void restablecerPassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String nuevaPassword = request.getParameter("nuevaPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        try {
            // Validaciones
            if (!nuevaPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.setAttribute("token", token);
                request.setAttribute("paso", "restablecer");
                request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
                return;
            }
            
            if (nuevaPassword.length() < 8) {
                request.setAttribute("error", "La contraseña debe tener al menos 8 caracteres");
                request.setAttribute("token", token);
                request.setAttribute("paso", "restablecer");
                request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
                return;
            }
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDAO.ResultSetToken tokenInfo = usuarioDAO.validarToken(token);
            
            if (tokenInfo == null || !"recuperacion".equals(tokenInfo.getTipo())) {
                request.setAttribute("error", "Token inválido o expirado");
                request.setAttribute("paso", "solicitar");
                request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
                return;
            }
            
            // Actualizar contraseña
            String nuevoHash = hashPassword(nuevaPassword);
            if (usuarioDAO.actualizarPassword(tokenInfo.getUsuarioId(), nuevoHash)) {
                usuarioDAO.marcarTokenComoUsado(token);
                request.setAttribute("mensaje", "Contraseña restablecida exitosamente. Ya puedes iniciar sesión.");
                request.setAttribute("paso", "completado");
            } else {
                request.setAttribute("error", "Error al restablecer la contraseña");
                request.setAttribute("paso", "restablecer");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            request.setAttribute("paso", "restablecer");
        }
        
        request.getRequestDispatcher("recuperacionPassword.jsp").forward(request, response);
    }
    
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        
        return hexString.toString();
    }
}
