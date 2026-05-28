package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

import dao.UsuarioDAO;
import models.Usuario;

public class Menu_Inicial extends AppCompatActivity {

    private ImageView iconUser;
    private Usuario usuario;
    private UsuarioDAO dao;
    private MaterialCardView parking, reservas, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_inicial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        this.iconUser = findViewById(R.id.id_icon_menu);
        this.parking = findViewById(R.id.id_btn_menu_parking);
        this.reservas = findViewById(R.id.id_btn_menu_reserv);
        this.info = findViewById(R.id.id_btn_menu_account);

        cargarIcono();
    }

    public void cargarIcono() {
        this.dao = new UsuarioDAO();
        String icono = this.dao.buscarIcono(this.usuario.getIdUsuario());
        switch (icono) {
            case "icono_azul":
                this.iconUser.setImageResource(R.mipmap.icono_azul);
                this.dao.cerrarConexion();
                break;
            case "icono_rosa":
                this.iconUser.setImageResource(R.mipmap.icono_rosa);
                this.dao.cerrarConexion();
                break;
            case "icono_rojo":
                this.iconUser.setImageResource(R.mipmap.icono_rojo);
                this.dao.cerrarConexion();
                break;
            case "icono_verde":
                this.iconUser.setImageResource(R.mipmap.icono_verde);
                this.dao.cerrarConexion();
                break;
            default:
                this.iconUser.setImageResource(R.mipmap.icono_default);
                this.dao.cerrarConexion();
                break;
        }
    }

    public void salirApp(View view) {
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
    }

    public void buscarParkings(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void listaReservas(View view) {
        Intent intent = new Intent(this, Lista_Reservas.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void cuentaUsuario(View view) {
        Intent intent = new Intent(this, Info_Usuario.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }
}