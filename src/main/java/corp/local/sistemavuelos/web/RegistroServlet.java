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

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        
        System.out.println("=== INICIANDO PROCESO DE REGISTRO ===");
        System.out.println("Email: " + email);
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        
        try {
            // Validaciones
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                System.out.println("ERROR: Las contraseñas no coinciden");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
            
            // VALIDACIÓN COMPLETA DE CONTRASEÑA
            String passwordError = validarPassword(password);
            if (passwordError != null) {
                request.setAttribute("error", passwordError);
                System.out.println("ERROR en contraseña: " + passwordError);
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Verificar si el email ya existe
            if (usuarioDAO.buscarPorEmail(email) != null) {
                request.setAttribute("error", "El email ya está registrado");
                System.out.println("ERROR: Email ya registrado");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
            
            // Crear usuario
            String passwordHash = hashPassword(password);
            Usuario usuario = new Usuario(email, passwordHash, nombre, apellido);
            
            if (usuarioDAO.registrarUsuario(usuario)) {
                // Obtener el usuario recién registrado para tener el ID
                Usuario usuarioRegistrado = usuarioDAO.buscarPorEmail(email);
                
                if (usuarioRegistrado != null) {
                    // Generar token de verificación
                    String token = usuarioDAO.generarToken(usuarioRegistrado.getId(), "verificacion");
                    
                    if (token != null) {
                        // Crear enlace de verificación COMPLETO
                        String baseUrl = getBaseUrl(request);
                        String verificationLink = baseUrl + "verificarEmail?token=" + token;
                        
                        System.out.println("=== ENVIANDO EMAIL DE VERIFICACIÓN ===");
                        System.out.println("PARA: " + email);
                        System.out.println("ENLACE: " + verificationLink);
                        System.out.println("TOKEN: " + token);
                        
                        // ENVIAR EMAIL REAL DE VERIFICACIÓN
                        EmailService emailService = new EmailService();
                        boolean emailEnviado = emailService.enviarEmailVerificacion(email, verificationLink, nombre);
                        
                        if (emailEnviado) {
                            System.out.println("✅ EMAIL DE VERIFICACIÓN ENVIADO EXITOSAMENTE");
                            request.setAttribute("mensaje", "Registro exitoso. Se ha enviado un email de verificación a tu correo.");
                        } else {
                            System.out.println("❌ FALLÓ EL ENVÍO DEL EMAIL DE VERIFICACIÓN");
                            // Mostrar el enlace en la página como fallback
                            request.setAttribute("enlaceVerificacion", verificationLink);
                            request.setAttribute("tokenVerificacion", token);
                            request.setAttribute("mensaje", "Registro exitoso, pero no pudimos enviar el email de verificación. Usa el enlace mostrado abajo.");
                        }
                    } else {
                        request.setAttribute("error", "Error al generar token de verificación");
                        System.out.println("ERROR: No se pudo generar token");
                    }
                } else {
                    request.setAttribute("error", "Error al obtener usuario registrado");
                    System.out.println("ERROR: No se pudo obtener usuario después del registro");
                }
            } else {
                request.setAttribute("error", "Error al registrar usuario");
                System.out.println("ERROR: No se pudo registrar en BD");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor: " + e.getMessage());
            System.out.println("ERROR en servidor: " + e.getMessage());
        }
        
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
    
    // Método para obtener la URL base correctamente
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); // http o https
        String serverName = request.getServerName(); // localhost o dominio
        int serverPort = request.getServerPort(); // 8080, 80, etc.
        String contextPath = request.getContextPath(); // /SistemaVuelos
        
        // Reconstruir la URL base
        String baseUrl = scheme + "://" + serverName;
        
        // Solo agregar puerto si no es el estándar (80 para http, 443 para https)
        if (("http".equals(scheme) && serverPort != 80) || 
            ("https".equals(scheme) && serverPort != 443)) {
            baseUrl += ":" + serverPort;
        }
        
        baseUrl += contextPath + "/";
        
        System.out.println("URL base construída: " + baseUrl);
        return baseUrl;
    }
    
    // MÉTODO MEJORADO PARA VALIDAR CONTRASEÑA
    private String validarPassword(String password) {
        System.out.println("Validando contraseña: " + password);
        
        if (password == null || password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }
        
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                hasSpecialChar = true;
                System.out.println("✓ Carácter especial detectado: '" + c + "'");
            }
        }
        
        if (!hasUpperCase) {
            return "La contraseña debe incluir al menos una letra mayúscula";
        }
        
        if (!hasLowerCase) {
            return "La contraseña debe incluir al menos una letra minúscula";
        }
        
        if (!hasDigit) {
            return "La contraseña debe incluir al menos un número";
        }
        
        if (!hasSpecialChar) {
            return "La contraseña debe incluir al menos un carácter especial (cualquier símbolo)";
        }
        
        System.out.println("✓ Contraseña válida - Cumple todas las reglas");
        return null;
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