package models;

import java.io.Serializable;

public class Parking implements Serializable {

    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private double calificacion;
    private String placeId;

    public Parking(String nombre, String direccion, double lat, double lng, double calificacion, String place) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = lat;
        this.longitud = lng;
        this.calificacion = calificacion;
        this.placeId = place;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getPlaceId() {
        return placeId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
