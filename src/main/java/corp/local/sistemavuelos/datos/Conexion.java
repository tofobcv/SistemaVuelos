package corp.local.sistemavuelos.datos;

import java.sql.*;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class Conexion {
 private static final String JDBC_URL = 
"jdbc:mysql://localhost:3306/sistema_vuelos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
 private static final String JDBC_USER = "root";
 private static final String JDBC_PASSWORD = "gato"; // Cambia si tu password es diferente
 
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