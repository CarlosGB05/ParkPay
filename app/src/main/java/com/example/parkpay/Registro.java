package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Usuario;

public class Registro extends AppCompatActivity {

    private EditText text_name,text_phone, text_username,text_email, text_passw, text_confPassw;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.text_name = findViewById(R.id.id_text_register_name);
        this.text_phone = findViewById(R.id.id_text_register_phone);
        this.text_username = findViewById(R.id.id_text_register_username);
        this.text_email = findViewById(R.id.id_text_register_email);
        this.text_passw = findViewById(R.id.id_text_register_passw);
        this.text_confPassw = findViewById(R.id.id_text_register_confPassw);
        this.usuario = null;
    }

    public void crearUsuario(View view) {
        String infoName = this.text_name.getText().toString();
        String infoPhone = this.text_phone.getText().toString();
        String infoUsername = this.text_username.getText().toString();
        String infoEmail = this.text_email.getText().toString();
        String infoPassw = this.text_passw.getText().toString();
        String infoConfPassw = this.text_confPassw.getText().toString();
        String info = "Rellene los datos";

        if ( infoName.isEmpty() || infoPhone.isEmpty() || infoUsername.isEmpty() ||
                infoEmail.isEmpty() || infoPassw.isEmpty() || infoConfPassw.isEmpty()) {
            this.text_name.setError(info);
            this.text_phone.setError(info);
            this.text_username.setError(info);
            this.text_email.setError(info);
            this.text_passw.setError(info);
            this.text_confPassw.setError(info);
        } else {
            // Mas adelante añador DAO y NOTIFICACION EMAIL DE REGISTRO
            Intent intent = new Intent(this, Login.class);
            this.usuario = new Usuario(infoName, Integer.valueOf(infoPhone), infoUsername,
                    infoEmail, infoPassw);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
        }
    }
}