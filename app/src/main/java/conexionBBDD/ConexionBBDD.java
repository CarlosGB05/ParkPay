package conexionBBDD;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {

    private Connection conexion = null;
    private static final String driver = "org.postgresql.Driver";
    private static final String url = "jdbc:postgresql://10.0.2.2/ParkPay";      // IP Generica
    private static final String user = "postgres";
    private static final String password = "1234";
    private Connection connection;

    public Connection conectarBD() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(driver);
            conexion = DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conexion;

    }

    protected void cerrarConexion(Connection conn) throws Exception{
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
