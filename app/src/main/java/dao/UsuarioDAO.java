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
                    + "(nombreCompleto, email, telefono, dni, contrasena) "
                    + "values (?,?,?,?,?);";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, usuario.getNombreCompleto());
                this.sentenciaPara.setString(2, usuario.getEmail());
                this.sentenciaPara.setInt(3, usuario.getTelefono());
                this.sentenciaPara.setString(4, usuario.getDni());
                this.sentenciaPara.setString(5, usuario.getPassword());

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
                            this.result.getInt("idUsuario"),
                            this.result.getString("nombreCompleto"),
                            this.result.getString("email"),
                            this.result.getInt("telefono"),
                            this.result.getString("dni"),
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

    public boolean comprobarEmail(String email) {
        if(email != null){
            String queryInsert = "Select count(*) as total from usuarios where email = ?;";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, email);

                this.result = this.sentenciaPara.executeQuery();
                if (this.result.next()) {
                    int total = this.result.getInt("total");
                    return total > 0;
                }

            } catch (SQLException e) {
                System.out.println("Error al actualizar el Usuarios");
                e.printStackTrace();
            }
        }
        else {
            return false;
        }
        return false;
    }

    public boolean actualizarUsuario(Usuario usuario){
        if(usuario != null){
            String queryInsert = "Update usuarios set nombreCompleto = ?," +
                    " telefono = ?, dni = ?, contrasena = ? where email = ?;";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, usuario.getNombreCompleto());
                this.sentenciaPara.setInt(2, usuario.getTelefono());
                this.sentenciaPara.setString(3,usuario.getDni());
                this.sentenciaPara.setString(4, usuario.getPassword());
                this.sentenciaPara.setString(5, usuario.getEmail());

                this.sentenciaPara.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("Error al actualizar el Usuarios");
                e.printStackTrace();
            }
        }
        else {
            return false;
        }
        return false;
    }

    public boolean insertarIconoUsuario(Integer id){
        try {
            String queryInsert = "INSERT INTO iconos_usuarios "
                    + "(idUsuario_Fk, nombreIcono) values (?,'icono_default');";


            this.sentenciaPara = connection.prepareStatement(queryInsert);
            this.sentenciaPara.setInt(1, id);

            this.sentenciaPara.executeUpdate();
            System.out.println("Icono Usuario insertado correctamente");
            return true;

        } catch (SQLException e) {
                System.out.println("Error al insertar el Usuario");
                e.printStackTrace();
        }
        return false;
    }

    public String buscarIcono(Integer id) {
        if(id > 0){
            try {
                String query = "Select nombreIcono from iconos_usuarios where idUsuario_Fk = ?;";

                this.sentenciaPara = this.connection.prepareStatement(query);
                this.sentenciaPara.setInt(1,id);

                this.result = this.sentenciaPara.executeQuery();
                while (this.result.next()) {
                    String info = this.result.getString("nombreIcono");
                    return info;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
        return null;
    }

    public boolean actualizarIcono(Integer id, String icono) {
        if (id > 0 && !icono.isEmpty()) {
            String queryInsert = "Update iconos_usuarios set nombreIcono = ? "
                    + "where idUsuario_Fk = ?;";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setString(1, icono);
                this.sentenciaPara.setInt(2, id);

                this.sentenciaPara.executeUpdate();
                System.out.println("Icono Usuario insertado correctamente");
                return true;

            } catch (SQLException e) {
                System.out.println("Error al insertar el Icono Usuario");
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

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
