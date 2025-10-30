package corp.local.sistemavuelos.sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class ManejadorCliente implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clienteId;
    
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
        this.clienteId = "CLIENTE_" + socket.getInetAddress() + ":" + socket.getPort();
    }
    
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("🎯 Cliente conectado: " + clienteId);
            
            // Enviar mensaje de bienvenida
            enviarMensaje("BIENVENIDA:Conectado al Servidor de Vuelos");
            
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("📨 Mensaje recibido de " + clienteId + ": " + mensaje);
                procesarMensaje(mensaje);
            }
        } catch (IOException e) {
            System.out.println("🔌 Cliente desconectado: " + clienteId);
        } finally {
            try {
                ServidorPrincipal.removerCliente(this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void procesarMensaje(String mensaje) {
        try {
            // Procesar mensajes simples (sin JSON)
            if (mensaje.startsWith("BUSCAR_VUELOS:")) {
                procesarBusquedaVuelos(mensaje);
            } else if (mensaje.startsWith("ESTADO_RESERVA:")) {
                procesarEstadoReserva(mensaje);
            } else if (mensaje.startsWith("MENSAJE:")) {
                procesarMensajeSimple(mensaje);
            } else {
                // Echo para mensajes simples
                enviarMensaje("ECHO:" + mensaje);
            }
        } catch (Exception e) {
            enviarMensaje("ERROR:Error procesando mensaje: " + e.getMessage());
        }
    }
    
    private void procesarBusquedaVuelos(String mensaje) {
        try {
            // Formato: BUSCAR_VUELOS:origen,destino,fecha
            String[] partes = mensaje.split(":");
            if (partes.length >= 2) {
                String[] datos = partes[1].split(",");
                if (datos.length >= 3) {
                    String origen = datos[0];
                    String destino = datos[1];
                    String fecha = datos[2];
                    
                    System.out.println("🔍 Búsqueda solicitada: " + origen + " → " + destino + " (" + fecha + ")");
                    
                    // Simular resultados de vuelos
                    String respuesta = "RESULTADOS_VUELOS:" +
                        "Aeroméxico,AM500,2500,08:00,2h 30m|" +
                        "Volaris,Y4123,1800,14:20,2h 25m";
                    
                    enviarMensaje(respuesta);
                    return;
                }
            }
            enviarMensaje("ERROR:Formato inválido para búsqueda");
            
        } catch (Exception e) {
            enviarMensaje("ERROR:Error en búsqueda: " + e.getMessage());
        }
    }
    
    private void procesarEstadoReserva(String mensaje) {
        try {
            // Formato: ESTADO_RESERVA:codigo
            String[] partes = mensaje.split(":");
            if (partes.length >= 2) {
                String codigoReserva = partes[1];
                
                String respuesta = "ESTADO_RESERVA:" +
                    codigoReserva + ",confirmada,AM500 - Ciudad de México → Cancún,2024-06-15 08:00,2";
                
                enviarMensaje(respuesta);
                return;
            }
            enviarMensaje("ERROR:Formato inválido para estado de reserva");
            
        } catch (Exception e) {
            enviarMensaje("ERROR:Error consultando reserva: " + e.getMessage());
        }
    }
    
    private void procesarMensajeSimple(String mensaje) {
        // Formato: MENSAJE:texto
        String[] partes = mensaje.split(":");
        if (partes.length >= 2) {
            String texto = partes[1];
            enviarMensaje("RESPUESTA:Recibido: " + texto);
        } else {
            enviarMensaje("ERROR:Formato inválido para mensaje");
        }
    }
    
    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
            System.out.println("📤 Enviado a " + clienteId + ": " + mensaje);
        }
    }
}