package corp.local.sistemavuelos.datos;

import java.sql.*;
import java.util.*;

public class HistorialDAO {
    
    private static final String SQL_INSERT_BUSQUEDA = "INSERT INTO historial_busquedas(usuario_id, origen, destino, fecha_ida, fecha_vuelta, pasajeros) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BUSQUEDAS = "SELECT * FROM historial_busquedas WHERE usuario_id = ? ORDER BY fecha_busqueda DESC LIMIT 10";
    
    public boolean guardarBusqueda(int usuarioId, String origen, String destino, String fechaIda, String fechaVuelta, int pasajeros) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_BUSQUEDA);
            stmt.setInt(1, usuarioId);
            stmt.setString(2, origen);
            stmt.setString(3, destino);
            stmt.setString(4, fechaIda);
            stmt.setString(5, fechaVuelta);
            stmt.setInt(6, pasajeros);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException ex) {
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
    
    public List<Object[]> obtenerHistorialBusquedas(int usuarioId) {
        List<Object[]> busquedas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BUSQUEDAS);
            stmt.setInt(1, usuarioId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] busqueda = new Object[7];
                busqueda[0] = rs.getInt("id");
                busqueda[1] = rs.getString("origen");
                busqueda[2] = rs.getString("destino");
                busqueda[3] = rs.getDate("fecha_ida");
                busqueda[4] = rs.getDate("fecha_vuelta");
                busqueda[5] = rs.getInt("pasajeros");
                busqueda[6] = rs.getTimestamp("fecha_busqueda");
                busquedas.add(busqueda);
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
        return busquedas;
    }
}