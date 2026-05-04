package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Usuario;

public class Menu_Inicial extends AppCompatActivity {

    private TextView text_username;
    private Usuario usuario;

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

        this.text_username = findViewById(R.id.id_text_username_menu);
//        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
//        this.text_username.setText(this.usuario.getNombreUsuario());
    }

    public void salirApp(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buscarParkings(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        startActivity(intent);
    }

    public void cuentaUsuario(View view) {
        Intent intent = new Intent(this, Info_Usuario.class);
        startActivity(intent);
    }
}