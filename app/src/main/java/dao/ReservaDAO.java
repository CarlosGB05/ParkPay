package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conexionBBDD.ConexionBBDD;
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
}
