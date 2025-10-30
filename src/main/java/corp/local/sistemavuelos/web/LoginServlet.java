package corp.local.sistemavuelos.web;

import corp.local.sistemavuelos.datos.UsuarioDAO;
import corp.local.sistemavuelos.dominio.Usuario;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        System.out.println("=== INICIANDO PROCESO DE LOGIN ===");
        System.out.println("Email: " + email);
        
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            
            if (usuario == null) {
                request.setAttribute("error", "Credenciales incorrectas");
                System.out.println("ERROR: Usuario no encontrado");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            String passwordHash = hashPassword(password);
            
            if (!usuario.getPasswordHash().equals(passwordHash)) {
                request.setAttribute("error", "Credenciales incorrectas");
                System.out.println("ERROR: Contraseña incorrecta");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            if (!usuario.isEmailVerificado()) {
                request.setAttribute("error", "Debes verificar tu email antes de iniciar sesión");
                System.out.println("ERROR: Email no verificado");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            // Crear sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioEmail", usuario.getEmail());
            session.setAttribute("usuarioNombre", usuario.getNombre() + " " + usuario.getApellido());
            
            System.out.println("✓ Login exitoso para: " + usuario.getEmail());
            
            // Redirigir a página principal
            response.sendRedirect("index.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            System.out.println("ERROR en servidor: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
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