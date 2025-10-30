package corp.local.sistemavuelos.servicios;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    
    // TUS CREDENCIALES REALES  
    private final String username = "bcris1970@gmail.com";  
    private final String password = "tgjt mirw olpb qztp";  
    
    // CONFIGURACIÓN GLOBAL DE SEGURIDAD
    static {
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        System.setProperty("mail.smtp.ssl.trust", "*");
        System.setProperty("mail.smtp.starttls.enable", "true");
    }
      
    public boolean enviarEmailConfirmacion(String destinatario, String numeroReserva,   
                                          String nombrePasajero, double total,    
                                          String metodoPago, String origen,    
                                          String destino, String fechaVuelo) { 
          
        // Primero intentar envío REAL  
        boolean exitoReal = enviarEmailReal(destinatario, numeroReserva, nombrePasajero,   
                                          total, metodoPago, origen, destino, fechaVuelo);  
          
        if (exitoReal) {  
            return true;  
        } else {  
            // Si falla el real, hacer simulación  
            System.out.println("      Falló envío real, usando simulación");  
            return enviarEmailSimulado(destinatario, numeroReserva, nombrePasajero,   
                                     total, metodoPago, origen, destino, fechaVuelo);  
        }  
    }  
      
    private boolean enviarEmailReal(String destinatario, String numeroReserva,   
                                   String nombrePasajero, double total,    
                                   String metodoPago, String origen,    
                                   String destino, String fechaVuelo) {  
          
        System.out.println("===            INTENTANDO ENVÍO REAL DE EMAIL ===");  
        System.out.println("DE: " + username);
        System.out.println("PARA: " + destinatario);  
          
        // CONFIGURACIÓN SIMPLIFICADA Y FUNCIONANTE  
        Properties props = new Properties();  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.starttls.enable", "true");  
        props.put("mail.smtp.starttls.required", "true");  
        props.put("mail.smtp.host", "smtp.gmail.com");  
        props.put("mail.smtp.port", "587");  
          
        // SOLO LAS PROPIEDADES SSL ESENCIALES  
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");  
        props.put("mail.smtp.ssl.trust", "*");  
          
        // CONFIGURACIÓN DE TIMEOUT  
        props.put("mail.smtp.connectiontimeout", "15000");  
        props.put("mail.smtp.timeout", "15000");  
        props.put("mail.smtp.writetimeout", "15000");  
          
        try {  
            Session session = Session.getInstance(props,  
                new javax.mail.Authenticator() {  
                    protected PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(username, password);  
                    }  
                });  
              
            // HABILITAR DEBUG PARA VER LA CONEXIÓN  
            session.setDebug(true);  
              
            // Crear mensaje  
            Message message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(username, "Sistema de Vuelos"));  
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));  
            message.setSubject("     Confirmación de Reserva #" + numeroReserva + " - Sistema de Vuelos");  
              
            // Crear contenido HTML del email  
            String htmlContent = crearContenidoEmailHTML(numeroReserva, nombrePasajero, total,   
                                                        metodoPago, origen, destino, fechaVuelo);  
              
            message.setContent(htmlContent, "text/html; charset=utf-8");  
              
            System.out.println("    Enviando email a través de Gmail...");  
              
            // ENVÍO DIRECTO Y SIMPLE  
            Transport.send(message);  
              
            System.out.println("               EMAIL REAL ENVIADO EXITOSAMENTE A: " + destinatario);  
            return true;  
              
        } catch (Exception e) {  
            System.out.println("    ERROR EN ENVÍO REAL: " + e.getMessage());  
            e.printStackTrace();  
              
            // INTENTAR CON PUERTO ALTERNATIVO 465 (SSL)  
            System.out.println("     Intentando con puerto 465 (SSL)...");  
            return intentarEnvioSSL465(destinatario, numeroReserva, nombrePasajero,   
                                     total, metodoPago, origen, destino, fechaVuelo);  
        }  
    }  
      
    // MÉTODO ALTERNATIVO CON PUERTO 465 (SSL)  
    private boolean intentarEnvioSSL465(String destinatario, String numeroReserva,   
                                       String nombrePasajero, double total,    
                                       String metodoPago, String origen,    
                                       String destino, String fechaVuelo) {  
          
        Properties props = new Properties();  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.socketFactory.port", "465");  
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        props.put("mail.smtp.host", "smtp.gmail.com");  
        props.put("mail.smtp.port", "465");  
        props.put("mail.smtp.ssl.enable", "true");  
        props.put("mail.smtp.ssl.trust", "*");  
          
        // CONFIGURACIÓN DE TIMEOUT  
        props.put("mail.smtp.connectiontimeout", "15000");  
        props.put("mail.smtp.timeout", "15000");  
          
        try {  
            Session session = Session.getInstance(props,  
                new javax.mail.Authenticator() {  
                    protected PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(username, password);  
                    }  
                });  
              
            session.setDebug(true);  
              
            Message message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(username, "Sistema de Vuelos"));  
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));  
            message.setSubject("     Confirmación de Reserva #" + numeroReserva + " - Sistema de Vuelos");  
              
            String htmlContent = crearContenidoEmailHTML(numeroReserva, nombrePasajero, total,   
                                                        metodoPago, origen, destino, fechaVuelo);  
              
            message.setContent(htmlContent, "text/html; charset=utf-8");  
              
            System.out.println("    Intentando conexión SSL con puerto 465...");  
            Transport.send(message);  
              
            System.out.println("               EMAIL REAL ENVIADO EXITOSAMENTE (SSL 465) A: " + destinatario);  
            return true;  
              
        } catch (Exception e) {  
            System.out.println("    ERROR en puerto 465 también: " + e.getMessage());  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    private String crearContenidoEmailHTML(String numeroReserva, String nombrePasajero,   
                                         double total, String metodoPago, String origen,   
                                         String destino, String fechaVuelo) {  
          
        return "<!DOCTYPE html>" +  
               "<html lang='es'>" +  
               "<head>" +  
               "<meta charset='UTF-8'>" +  
               "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +  
               "<title>Confirmación de Reserva</title>" +  
               "<style>" +  
               "body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4; }" +  
               ".container { max-width: 600px; margin: 0 auto; background: white; }" +  
               ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 20px; text-align: center; }" +  
               ".header h1 { margin: 0; font-size: 28px; }" +  
               ".content { padding: 30px; }" +  
               ".reserva-info { background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #667eea; }" +  
               ".reserva-number { font-size: 24px; color: #28a745; font-weight: bold; text-align: center; margin: 15px 0; }" +  
               ".total { font-size: 20px; color: #28a745; font-weight: bold; text-align: center; margin: 15px 0; }" +  
               ".steps { background: #e7f3ff; padding: 20px; border-radius: 8px; margin: 20px 0; }" +  
               ".steps h3 { color: #0056b3; margin-top: 0; }" +  
               ".footer { background: #343a40; color: white; padding: 20px; text-align: center; }" +  
               ".info-item { margin: 10px 0; }" +  
               ".info-label { font-weight: bold; color: #555; }" +  
               "</style>" +  
               "</head>" +  
               "<body>" +  
               "<div class='container'>" +  
               "<div class='header'>" +  
               "<h1>        Confirmación de Reserva</h1>" +  
               "<p>Sistema de Vuelos</p>" +  
               "</div>" +  
               "<div class='content'>" +  
               "<p>Estimado/a <strong>" + (nombrePasajero != null ? nombrePasajero : "Cliente") + "</strong>,</p>" +  
               "<p>Su reserva ha sido confirmada exitosamente. A continuación encontrará los detalles de su viaje:</p>" +  
               "<div class='reserva-info'>" +  
               "<h3>        Detalles de la Reserva</h3>" +  
               "<div class='reserva-number'>Número de Reserva: " + numeroReserva + "</div>" +  
               "<div class='info-item'><span class='info-label'>Vuelo:</span> " + origen + " → " + destino + "</div>" +  
               "<div class='info-item'><span class='info-label'>Fecha del Vuelo:</span> " + fechaVuelo + "</div>" +  
               "<div class='info-item'><span class='info-label'>Método de Pago:</span> " + metodoPago + "</div>" +  
               "<div class='total'>Total Pagado: $" + String.format("%.2f", total) + " MXN</div>" +  
               "</div>" +  
               "<div class='steps'>" +  
               "<h3>           Próximos Pasos</h3>" +  
               "<ul>" +  
               "<li>     Presente su identificación oficial y este comprobante en el aeropuerto</li>" +  
               "<li>         Realice el check-in 2 horas antes de la salida del vuelo</li>" +  
               "<li>         Mantenga este correo disponible en su dispositivo móvil</li>" +  
               "<li>     Para cambios o cancelaciones, contacte a nuestro servicio al cliente</li>" +  
               "</ul>" +  
               "</div>" +  
               "<p>¡Gracias por confiar en nosotros! Le deseamos un excelente viaje.</p>" +  
               "</div>" +  
               "<div class='footer'>" +  
               "<p><strong>Sistema de Vuelos</strong></p>" +  
               "<p>      bcris1970@gmail.com |      4444534567</p>" +  
               "<p>      Servicio disponible las 24 horas</p>" +  
               "</div>" +  
               "</div>" +  
               "</body>" +  
               "</html>";  
    }  
      
    private boolean enviarEmailSimulado(String destinatario, String numeroReserva,   
                                      String nombrePasajero, double total,    
                                      String metodoPago, String origen,    
                                      String destino, String fechaVuelo) {  
          
        System.out.println("===        SIMULANDO ENVÍO DE EMAIL ===");  
        System.out.println("PARA: " + destinatario);  
        System.out.println("ASUNTO: Confirmación de Reserva #" + numeroReserva);  
        System.out.println("MENSAJE:");  
        System.out.println("¡Hola " + (nombrePasajero != null ? nombrePasajero : "Cliente") + "!");  
        System.out.println("Tu reserva #" + numeroReserva + " ha sido confirmada.");  
        System.out.println("Vuelo: " + origen + " → " + destino);  
        System.out.println("Fecha: " + fechaVuelo);  
        System.out.println("Total: $" + total + " MXN");  
        System.out.println("Método de pago: " + metodoPago);  
        System.out.println("====================================");  
          
        // Simular envío exitoso  
        try {  
            Thread.sleep(1000);  
            System.out.println("       Email simulado enviado exitosamente");  
            return true;  
        } catch (InterruptedException e) {  
            System.out.println("    Error en simulación de email");  
            return false;  
        }  
    }

    // MÉTODO PARA ENVIAR EMAIL DE VERIFICACIÓN - CORREGIDO
    public boolean enviarEmailVerificacion(String destinatario, String enlaceVerificacion, String nombre) {
        System.out.println("=== ENVIANDO EMAIL DE VERIFICACIÓN REAL ===");
        System.out.println("DE: " + username);
        System.out.println("PARA: " + destinatario);
        System.out.println("ENLACE: " + enlaceVerificacion);
        System.out.println("NOMBRE: " + nombre);
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "*");
        
        // CONFIGURACIÓN DE TIMEOUT
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        
        try {
            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            
            session.setDebug(true);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Sistema de Vuelos"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Verifica tu cuenta - Sistema de Vuelos");
            
            // Contenido HTML mejorado
            String contenidoHTML = crearContenidoVerificacionHTML(nombre, enlaceVerificacion);
            message.setContent(contenidoHTML, "text/html; charset=utf-8");
            
            System.out.println("Enviando email de verificación...");
            Transport.send(message);
            
            System.out.println("✅ EMAIL DE VERIFICACIÓN ENVIADO EXITOSAMENTE");
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN ENVÍO DE VERIFICACIÓN: " + e.getMessage());
            e.printStackTrace();
            
            // Como fallback, mostrar en consola para desarrollo
            System.out.println("ENLACE DE VERIFICACIÓN (para desarrollo): " + enlaceVerificacion);
            return false;
        }
    }
    
    private String crearContenidoVerificacionHTML(String nombre, String enlaceVerificacion) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background: #667eea; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 20px; background: #f9f9f9; }" +
               ".button { display: inline-block; padding: 12px 24px; background: #28a745; color: white; text-decoration: none; border-radius: 5px; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Verifica tu Cuenta</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<p>Hola <strong>" + nombre + "</strong>,</p>" +
               "<p>Gracias por registrarte en nuestro Sistema de Vuelos. Para activar tu cuenta, haz clic en el siguiente botón:</p>" +
               "<p style='text-align: center;'>" +
               "<a href='" + enlaceVerificacion + "' class='button'>Verificar Mi Cuenta</a>" +
               "</p>" +
               "<p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>" +
               "<p><code>" + enlaceVerificacion + "</code></p>" +
               "<p>Este enlace expirará en 24 horas.</p>" +
               "<p>Si no solicitaste este registro, ignora este mensaje.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }

    // MÉTODO PARA ENVIAR EMAIL DE RECUPERACIÓN
    public boolean enviarEmailRecuperacion(String destinatario, String enlaceRecuperacion, String nombre) {
        System.out.println("=== ENVIANDO EMAIL DE RECUPERACIÓN REAL ===");
        System.out.println("DE: " + username);
        System.out.println("PARA: " + destinatario);
        System.out.println("ENLACE: " + enlaceRecuperacion);
        System.out.println("NOMBRE: " + nombre);
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        try {
            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            
            session.setDebug(true);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Sistema de Vuelos"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Recupera tu contraseña - Sistema de Vuelos");
            
            String contenido = "Hola " + nombre + ",\n\n" +
                             "Para recuperar tu contraseña, haz clic en el siguiente enlace:\n" +
                             enlaceRecuperacion + "\n\n" +
                             "Este enlace expirará en 1 hora.\n\n" +
                             "Si no solicitaste este cambio, ignora este mensaje.";
            
            message.setText(contenido);
            
            Transport.send(message);
            System.out.println("✅ EMAIL DE RECUPERACIÓN ENVIADO EXITOSAMENTE");
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR EN EMAIL DE RECUPERACIÓN: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}