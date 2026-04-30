package models;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario, telefono;
    private String nombreCompleto, nombreUsuario, email, dni, password;

    // Constructor para cuando se registra el Usuario
    public Usuario(String nc, String e, int tf, String nu, String pw) {
        this.nombreCompleto = nc;
        this.telefono = tf;
        this.nombreUsuario = nu;
        this.email = e;
        this.dni = "";
        this.password = pw;
    }

    // Constructor para cuando se busca y se encuentra un Usuario

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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
