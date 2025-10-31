package corp.local.sistemavuelos.datos;

import java.sql.*;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class Conexion {
    // CONFIGURACIÓN PARA AMBOS ENTORNOS
    private static final String DB_HOST = System.getenv("DB_HOST") != null ? 
        System.getenv("DB_HOST") : "localhost";
    
    private static final String JDBC_URL = 
        "jdbc:mysql://" + DB_HOST + ":3306/sistema_vuelos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static final String JDBC_USER = System.getenv("DB_USER") != null ? 
        System.getenv("DB_USER") : "root";
    
    private static final String JDBC_PASSWORD = System.getenv("DB_PASSWORD") != null ? 
        System.getenv("DB_PASSWORD") : "gato";
    
    private static BasicDataSource dataSource;
    
    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                dataSource = new BasicDataSource();
                dataSource.setUrl(JDBC_URL);
                dataSource.setUsername(JDBC_USER);
                dataSource.setPassword(JDBC_PASSWORD);
                dataSource.setInitialSize(5);
                dataSource.setMaxTotal(10);
                
                // LOG PARA DEBUGGING
                System.out.println("=== CONFIGURACIÓN BD ===");
                System.out.println("Host: " + DB_HOST);
                System.out.println("Usuario: " + JDBC_USER);
                System.out.println("URL: " + JDBC_URL);
                
                // Probar la conexión
                Connection testConn = dataSource.getConnection();
                System.out.println(" Conexión a BD exitosa");
                testConn.close();
                
            } catch (SQLException e) {
                System.out.println(" Error en conexión BD: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dataSource;
    }
    
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
    
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) rs.close();
    }
    
    public static void close(PreparedStatement stmt) throws SQLException {
        if (stmt != null) stmt.close();
    }
    
    public static void close(Connection conn) throws SQLException {
        if (conn != null) conn.close();
    }
}