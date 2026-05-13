package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.UsuarioDAO;
import models.Usuario;

public class Menu_Inicial extends AppCompatActivity {

    private TextView text_name;
    private ImageView iconUser;
    private Usuario usuario;
    private UsuarioDAO dao;

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

        this.text_name = findViewById(R.id.id_text_name_menu);
        this.iconUser = findViewById(R.id.id_icon_menu);
        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.text_name.setText(this.usuario.getNombreCompleto());
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
            default:
                this.iconUser.setImageResource(R.mipmap.icono_default);
                this.dao.cerrarConexion();
                break;
        }
    }

    public void salirApp(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buscarParkings(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void listaReservas(View view) {
        Intent intent = new Intent(this, Practicar_APIs.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void cuentaUsuario(View view) {
        Intent intent = new Intent(this, Info_Usuario.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }
}