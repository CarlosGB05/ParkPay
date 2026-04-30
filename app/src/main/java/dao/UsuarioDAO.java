package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexionBBDD.ConexionBBDD;
import models.Usuario;

public class UsuarioDAO {

    private Connection connection;
    private ConexionBBDD conexion = new ConexionBBDD();
    private Statement statement;
    private PreparedStatement sentenciaPara;
    private ResultSet result;

    public UsuarioDAO() {

        connection = conexion.conectarBD();
    }

    public boolean insertarUsuario(Usuario usuario){
        if(usuario != null){
            String queryInsert = "INSERT INTO usuarios "
                    + "(nombreCompleto, email, telefono, username, contrasena) "
                    + "values (?,?,?,?,?);";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, usuario.getNombreCompleto());
                this.sentenciaPara.setString(2, usuario.getEmail());
                this.sentenciaPara.setInt(3, usuario.getTelefono());
                this.sentenciaPara.setString(4, usuario.getNombreUsuario());
                this.sentenciaPara.setString(5, usuario.getNombreUsuario());

                this.sentenciaPara.executeUpdate();
                System.out.println("Usuario insertado correctamente");
                return true;

            } catch (SQLException e) {
                System.out.println("Error al insertar el Usuario");
                e.printStackTrace();
            }
        }
        return false;
    }

    public Usuario buscarUsuario(String correo){
        if(!correo.isEmpty()){
            String queryInsert = "Select * From usuarios where email = ?;";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, correo);

                this.result = this.sentenciaPara.executeQuery();
                while(this.result.next()){
                    Usuario usuario = new Usuario(
                            this.result.getString("nombreCompleto"),
                            this.result.getString("email"),
                            this.result.getInt("telefono"),
                            this.result.getString("username"),
                            this.result.getString("contrasena"));
                    return usuario;
                }

            } catch (SQLException e) {
                System.out.println("Error al buscar en tabla Usuarios");
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
        return null;
    }
//
//    public boolean actualizarUsuario(Usuario usuario){
//        if(usuario != null){
//            String queryInsert = "Update usuarios set nombre = ?, contrasena = ?," +
//                    " niveles_superados = ?, puntos_totales = ? where correo = ?;";
//            try {
//                this.sentenciaPara = connection.prepareStatement(queryInsert);
//                this.sentenciaPara.setString(1, usuario.getNombre());
//                this.sentenciaPara.setString(2, usuario.getPassword());
//                this.sentenciaPara.setInt(3,usuario.getNiveles());
//                this.sentenciaPara.setInt(4,usuario.getPuntosTotales());
//                this.sentenciaPara.setString(5, usuario.getCorreo());
//
//                this.sentenciaPara.executeUpdate();
//                return true;
//
//            } catch (SQLException e) {
//                System.out.println("Error al actualizar en tabla Usuarios");
//                e.printStackTrace();
//            }
//        }
//        else {
//            return false;
//        }
//        return false;
//    }
//
//    public ArrayList<Usuario> tierList(){
//        ArrayList<Usuario> lista = new ArrayList<>();
//        String queryInsert = "Select u.nombre, u.puntos_totales From usuarios as u" +
//                " ORDER BY u.puntos_totales DESC;";
//        try {
//            this.sentenciaPara = connection.prepareStatement(queryInsert);
//            this.result = this.sentenciaPara.executeQuery();
//            while(this.result.next() && lista.size() < 5){
//                Usuario usuario = new Usuario(
//                        this.result.getString("nombre"),
//                        this.result.getInt("puntos_totales"));
//                lista.add(usuario);
//            }
//            return lista;
//
//            } catch (SQLException e) {
//                System.out.println("Error al buscar en tabla Usuarios");
//                e.printStackTrace();
//            }
//        return null;
//    }

    public void cerrarConexion() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return connection;
    }

    public void setConnection (Connection conc) {
        connection = conc;
    }
}
