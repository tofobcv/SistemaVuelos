package corp.local.sistemavuelos.datos;

import java.sql.*;
import java.util.*;

public class ReservaDAO {
    
    private static final String SQL_INSERT_RESERVA = 
        "INSERT INTO reservas(codigo_reserva, usuario_id, vuelo_id, total_pagado, email_contacto, telefono_contacto, estado) VALUES(?, ?, ?, ?, ?, ?, 'confirmada')";
    
    private static final String SQL_SELECT_RESERVAS_USUARIO = 
        "SELECT r.*, v.numero_vuelo, v.origen, v.destino, v.fecha_salida, v.fecha_llegada, a.nombre as aerolinea_nombre " +
        "FROM reservas r " +
        "JOIN vuelos v ON r.vuelo_id = v.id " +
        "JOIN aerolineas a ON v.aerolinea_id = a.id " +
        "WHERE r.usuario_id = ? " +
        "ORDER BY r.fecha_reserva DESC";
    
    private static final String SQL_SELECT_RESERVA_POR_CODIGO = 
        "SELECT r.*, v.numero_vuelo, v.origen, v.destino, v.fecha_salida, v.fecha_llegada, a.nombre as aerolinea_nombre " +
        "FROM reservas r " +
        "JOIN vuelos v ON r.vuelo_id = v.id " +
        "JOIN aerolineas a ON v.aerolinea_id = a.id " +
        "WHERE r.codigo_reserva = ?";
    
    public boolean guardarReserva(String codigoReserva, int usuarioId, int vueloId, double total, 
                                 String emailContacto, String telefonoContacto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        System.out.println("=== EJECUTANDO GUARDAR RESERVA EN BD ===");
        System.out.println("Código: " + codigoReserva);
        System.out.println("Usuario ID: " + usuarioId);
        System.out.println("Vuelo ID: " + vueloId);
        System.out.println("Total: " + total);
        System.out.println("Email: " + emailContacto);
        System.out.println("Teléfono: " + telefonoContacto);
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_RESERVA);
            stmt.setString(1, codigoReserva);
            stmt.setInt(2, usuarioId);
            stmt.setInt(3, vueloId);
            stmt.setDouble(4, total);
            stmt.setString(5, emailContacto);
            stmt.setString(6, telefonoContacto);
            
            int rows = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + rows);
            
            boolean exito = rows > 0;
            if (exito) {
                System.out.println("✓ RESERVA INSERTADA CORRECTAMENTE EN BD");
            } else {
                System.out.println("✗ NO SE INSERTÓ NINGUNA FILA EN BD");
            }
            
            return exito;
            
        } catch (SQLException ex) {
            System.out.println("✗ ERROR SQL AL GUARDAR RESERVA: " + ex.getMessage());
            System.out.println("SQL State: " + ex.getSQLState());
            System.out.println("Error Code: " + ex.getErrorCode());
            ex.printStackTrace(System.out);
            return false;
        } finally {
            try {
                Conexion.close(stmt);
                Conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }
    
    public List<Object[]> obtenerReservasPorUsuario(int usuarioId) {
        List<Object[]> reservas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        System.out.println("=== BUSCANDO RESERVAS PARA USUARIO ID: " + usuarioId + " ===");
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_RESERVAS_USUARIO);
            stmt.setInt(1, usuarioId);
            rs = stmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                Object[] reserva = new Object[10];
                reserva[0] = rs.getString("codigo_reserva");
                reserva[1] = rs.getString("aerolinea_nombre");
                reserva[2] = rs.getString("numero_vuelo");
                reserva[3] = rs.getString("origen");
                reserva[4] = rs.getString("destino");
                reserva[5] = rs.getTimestamp("fecha_salida");
                reserva[6] = rs.getDouble("total_pagado");
                reserva[7] = rs.getString("estado");
                reserva[8] = rs.getTimestamp("fecha_reserva");
                reserva[9] = rs.getInt("id");
                
                reservas.add(reserva);
                System.out.println("Reserva " + count + ": " + reserva[0] + " - " + reserva[1] + " " + reserva[2]);
            }
            
            System.out.println("Total de reservas encontradas: " + count);
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener reservas: " + ex.getMessage());
            ex.printStackTrace(System.out);
        } finally {
            try {
                Conexion.close(rs);
                Conexion.close(stmt);
                Conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return reservas;
    }
    
    public Object[] obtenerReservaPorCodigo(String codigoReserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_RESERVA_POR_CODIGO);
            stmt.setString(1, codigoReserva);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Object[] reserva = new Object[10];
                reserva[0] = rs.getString("codigo_reserva");
                reserva[1] = rs.getString("aerolinea_nombre");
                reserva[2] = rs.getString("numero_vuelo");
                reserva[3] = rs.getString("origen");
                reserva[4] = rs.getString("destino");
                reserva[5] = rs.getTimestamp("fecha_salida");
                reserva[6] = rs.getDouble("total_pagado");
                reserva[7] = rs.getString("estado");
                reserva[8] = rs.getTimestamp("fecha_reserva");
                reserva[9] = rs.getInt("id");
                return reserva;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                Conexion.close(rs);
                Conexion.close(stmt);
                Conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return null;
    }
}