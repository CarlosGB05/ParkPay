package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexionBBDD.ConexionBBDD;
import models.Parking;
import models.Reserva;
import models.Usuario;

public class ReservaDAO {

    private Connection connection;
    private ConexionBBDD conexion = new ConexionBBDD();
    private Statement statement;
    private PreparedStatement sentenciaPara;
    private ResultSet result;

    public ReservaDAO() {

        connection = conexion.conectarBD();
    }

    public boolean insertarReserva(Reserva reserva){
        if(reserva != null){
            String queryInsert = "INSERT INTO reservas "
                    + "(idUsuario_Fk, nombre, ubicacion, calificacion, matricula, precioTotal, " +
                    "fechaReserva, inicioReserva, finalReserva, cocheElectrico) values (?,?,?,?,?,?,?,?,?,?);";
            try {
                this.sentenciaPara = connection.prepareStatement(queryInsert);
                this.sentenciaPara.setInt(1, reserva.getIdUsuario());
                this.sentenciaPara.setString(2, reserva.getNombreParking());
                this.sentenciaPara.setString(3, reserva.getUbicacionParking());
                this.sentenciaPara.setDouble(4, reserva.getCalificacionParking());
                this.sentenciaPara.setString(5, reserva.getMatricula());
                this.sentenciaPara.setDouble(6, reserva.getPrecioTotal());
                this.sentenciaPara.setString(7, reserva.getFechaReserva());
                this.sentenciaPara.setString(8, reserva.getInicioReserva());
                this.sentenciaPara.setString(9, reserva.getFinReserva());
                this.sentenciaPara.setBoolean(10, reserva.isCocheElectrico());

                this.sentenciaPara.executeUpdate();
                System.out.println("Reserva insertado correctamente");
                return true;

            } catch (SQLException e) {
                System.out.println("Error al insertar la reserva");
                e.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<Reserva> buscarReservas(int id){
        ArrayList<Reserva> lista = new ArrayList<>();
        String queryInsert = "Select * From reservas where idUsuario_Fk = ?;";
        try {
            this.sentenciaPara = connection.prepareStatement(queryInsert);
            this.sentenciaPara.setInt(1, id);

            this.result = this.sentenciaPara.executeQuery();
            while(this.result.next()){
                Reserva reserva = new Reserva(
                        this.result.getInt("idReserva"),
                        this.result.getInt("idUsuario_Fk"),
                        this.result.getString("nombre"),
                        this.result.getString("ubicacion"),
                        this.result.getString("matricula"),
                        this.result.getString("fechaReserva"),
                        this.result.getString("inicioReserva"),
                        this.result.getString("finalReserva"),
                        this.result.getDouble("calificacion"),
                        this.result.getDouble("precioTotal"),
                        this.result.getBoolean("cocheElectrico"));
                lista.add(reserva);
            }

            return lista;

        } catch (SQLException e) {
            System.out.println("Error al buscar en tabla Usuarios");
            e.printStackTrace();
        }
        return null;
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
