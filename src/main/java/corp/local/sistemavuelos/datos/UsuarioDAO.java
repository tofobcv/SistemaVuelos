package corp.local.sistemavuelos.datos;

import corp.local.sistemavuelos.dominio.Usuario;
import java.sql.*;
import java.util.UUID;

public class UsuarioDAO {
    
    private static final String SQL_INSERT = "INSERT INTO usuarios(email, password_hash, nombre, apellido) VALUES(?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM usuarios WHERE email = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM usuarios WHERE id = ?";
    private static final String SQL_UPDATE_VERIFICACION = "UPDATE usuarios SET email_verificado = TRUE, fecha_verificacion = NOW() WHERE id = ?";
    private static final String SQL_UPDATE_PASSWORD = "UPDATE usuarios SET password_hash = ? WHERE id = ?";
    
    // Token methods
    private static final String SQL_INSERT_TOKEN = "INSERT INTO tokens_verificacion(usuario_id, token, tipo, expiracion) VALUES(?, ?, ?, ?)";
    private static final String SQL_SELECT_TOKEN = "SELECT * FROM tokens_verificacion WHERE token = ? AND utilizado = FALSE AND expiracion > NOW()";
    private static final String SQL_MARCAR_TOKEN_USADO = "UPDATE tokens_verificacion SET utilizado = TRUE WHERE token = ?";
    private static final String SQL_INVALIDAR_TOKENS_ANTERIORES = "UPDATE tokens_verificacion SET utilizado = TRUE WHERE usuario_id = ? AND tipo = ? AND utilizado = FALSE";
    
    public boolean registrarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellido());
            
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
    
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMAIL);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPasswordHash(rs.getString("password_hash"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEmailVerificado(rs.getBoolean("email_verificado"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setFechaVerificacion(rs.getTimestamp("fecha_verificacion"));
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
        return usuario;
    }
    
    public Usuario buscarPorId(int id) {
        Usuario usuario = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPasswordHash(rs.getString("password_hash"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEmailVerificado(rs.getBoolean("email_verificado"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setFechaVerificacion(rs.getTimestamp("fecha_verificacion"));
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
        return usuario;
    }
    
    public boolean verificarEmail(int usuarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_VERIFICACION);
            stmt.setInt(1, usuarioId);
            
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
    
    public boolean actualizarPassword(int usuarioId, String nuevoHash) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_PASSWORD);
            stmt.setString(1, nuevoHash);
            stmt.setInt(2, usuarioId);
            
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
    
    // Métodos para tokens
    public String generarToken(int usuarioId, String tipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Invalidar tokens anteriores
            invalidarTokensAnteriores(usuarioId, tipo);
            
            // Generar nuevo token
            String token = UUID.randomUUID().toString();
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_TOKEN);
            stmt.setInt(1, usuarioId);
            stmt.setString(2, token);
            stmt.setString(3, tipo);
            
            // Token expira en 24 horas para verificación, 1 hora para recuperación
            if ("verificacion".equals(tipo)) {
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
            } else {
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() + 60 * 60 * 1000));
            }
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return token;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                Conexion.close(stmt);
                Conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return null;
    }
    
    public ResultSetToken validarToken(String token) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_TOKEN);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new ResultSetToken(rs.getInt("usuario_id"), rs.getString("tipo"));
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
    
    public boolean marcarTokenComoUsado(String token) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_MARCAR_TOKEN_USADO);
            stmt.setString(1, token);
            
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
    
    private void invalidarTokensAnteriores(int usuarioId, String tipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INVALIDAR_TOKENS_ANTERIORES);
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tipo);
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                Conexion.close(stmt);
                Conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }
    
    // Clase interna para resultado de token
    public static class ResultSetToken {
        private int usuarioId;
        private String tipo;
        
        public ResultSetToken(int usuarioId, String tipo) {
            this.usuarioId = usuarioId;
            this.tipo = tipo;
        }
        
        public int getUsuarioId() { return usuarioId; }
        public String getTipo() { return tipo; }
    }
}