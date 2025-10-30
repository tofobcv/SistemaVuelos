package corp.local.sistemavuelos.sockets;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServidorPrincipal {
    private static final int PUERTO_SOCKET = 9090;
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    private static List<ManejadorCliente> clientesConectados = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("🚀 Servidor Socket de Vuelos INICIANDO...");
        System.out.println("📍 Puerto: " + PUERTO_SOCKET);
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO_SOCKET)) {
            System.out.println("✅ Servidor escuchando en puerto: " + PUERTO_SOCKET);
            
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("🔗 Nuevo cliente conectado: " + clienteSocket.getInetAddress());
                
                ManejadorCliente manejador = new ManejadorCliente(clienteSocket);
                clientesConectados.add(manejador);
                pool.execute(manejador);
            }
        } catch (IOException e) {
            System.out.println("❌ Error en servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para enviar mensajes a todos los clientes
    public static void broadcast(String mensaje) {
        for (ManejadorCliente cliente : clientesConectados) {
            cliente.enviarMensaje(mensaje);
        }
    }
    
    // Método para eliminar cliente desconectado
    public static void removerCliente(ManejadorCliente cliente) {
        clientesConectados.remove(cliente);
    }
}