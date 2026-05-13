package models;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario, telefono;
    private String nombreCompleto, email, dni, password;

    // Constructor para cuando se registra el Usuario
    public Usuario(String nc, String e, int tf, String d, String pw) {
        this.nombreCompleto = nc;
        this.telefono = tf;
        this.dni = d;
        this.email = e;
        this.password = pw;
    }

    // Constructor con todos los parametros
    public Usuario(int id, String nc, String e, int tf, String d, String pw) {
        this.idUsuario = id;
        this.nombreCompleto = nc;
        this.email = e;
        this.telefono = tf;
        this.dni = d;
        this.password = pw;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
