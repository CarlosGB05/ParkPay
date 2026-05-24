package models;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {

    private int idReserva, idUsuario;
    private String nombreParking, ubicacionParking, matricula, fechaReserva, inicioReserva, finReserva;
    private double calificacionParking, precioTotal;
    private boolean cocheElectrico;

    // Constructor para crear Reserva
    public Reserva(int idUsuario, String nombreParking, String ubicacionParking,
                   double calificacionParking,  String fechaReserva, String inicioReserva, String finReserva,
                   double precioTotal, String matricula, boolean cocheElectrico) {
        this.idUsuario = idUsuario;
        this.nombreParking = nombreParking;
        this.ubicacionParking = ubicacionParking;
        this.matricula = matricula;
        this.fechaReserva = fechaReserva;
        this.inicioReserva = inicioReserva;
        this.finReserva = finReserva;
        this.calificacionParking = calificacionParking;
        this.precioTotal = precioTotal;
        this.cocheElectrico = cocheElectrico;
    }

    // Contructor con todos los parámetros
    public Reserva(int idReserva, int idUsuario, String nombreParking, String ubicacionParking,
                   String matricula, String fechaReserva, String inicioReserva, String finReserva,
                   double calificacionParking, double precioTotal, boolean cocheElectrico) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.nombreParking = nombreParking;
        this.ubicacionParking = ubicacionParking;
        this.matricula = matricula;
        this.fechaReserva = fechaReserva;
        this.inicioReserva = inicioReserva;
        this.finReserva = finReserva;
        this.calificacionParking = calificacionParking;
        this.precioTotal = precioTotal;
        this.cocheElectrico = cocheElectrico;
    }

    public int getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreParking() {
        return nombreParking;
    }
    public void setNombreParking(String nombreParking) {
        this.nombreParking = nombreParking;
    }

    public String getUbicacionParking() {
        return ubicacionParking;
    }
    public void setUbicacionParking(String ubicacionParking) {
        this.ubicacionParking = ubicacionParking;
    }

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }
    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getInicioReserva() {
        return inicioReserva;
    }
    public void setInicioReserva(String inicioReserva) {
        this.inicioReserva = inicioReserva;
    }

    public String getFinReserva() {
        return finReserva;
    }
    public void setFinReserva(String finReserva) {
        this.finReserva = finReserva;
    }

    public double getCalificacionParking() {
        return calificacionParking;
    }
    public void setCalificacionParking(double calificacionParking) {
        this.calificacionParking = calificacionParking;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public boolean isCocheElectrico() {
        return cocheElectrico;
    }
    public void setCocheElectrico(boolean cocheElectrico) {
        this.cocheElectrico = cocheElectrico;
    }
}
