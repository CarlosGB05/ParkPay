package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.UsuarioDAO;
import models.Usuario;

public class Registro extends AppCompatActivity {

    private EditText text_name,text_phone, text_username,text_email, text_passw, text_confPassw;
    private Usuario usuario;
    private UsuarioDAO dao;

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

        String valorTf = this.text_phone.getText().toString();
        Integer infoPhone;
        if(valorTf.isEmpty()) {
            infoPhone = 0;
        } else {
            infoPhone = Integer.parseInt(valorTf);
        }

        String infoUsername = this.text_username.getText().toString();
        String infoEmail = this.text_email.getText().toString();
        String infoPassw = this.text_passw.getText().toString();
        String infoConfPassw = this.text_confPassw.getText().toString();
        String infoEmpty = "Rellena todos los datos";
        String infoNameLongitud = "Máx 20 caracteres";
        String infoTelefLongitud = "Máx 9 Números";
        String infoEmailIncorrect = "No es un Email real";
        String infoPasswIncorrect = "No coinciden";

        if ((infoName.isEmpty() && infoPhone == 0 && infoUsername.isEmpty() &&
                infoEmail.isEmpty() && infoPassw.isEmpty() && infoConfPassw.isEmpty()) &&
                (infoName.isEmpty() || infoPhone == 0 || infoUsername.isEmpty() ||
                        infoEmail.isEmpty() || infoPassw.isEmpty() || infoConfPassw.isEmpty())) {
            this.text_name.setError(infoEmpty);
            this.text_phone.setError(infoEmpty);
            this.text_username.setError(infoEmpty);
            this.text_email.setError(infoEmpty);
            this.text_passw.setError(infoEmpty);
            this.text_confPassw.setError(infoEmpty);
            return;
        }

        if(infoName.length() > 20 || infoUsername.length() > 20) {
            this.text_name.setError(infoNameLongitud);
            this.text_username.setError(infoNameLongitud);
            return;
        }

        if(infoPhone.toString().length() < 9 || infoPhone.toString().length() > 9) {
            this.text_phone.setError(infoTelefLongitud);
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(infoEmail).matches()) {
            this.text_email.setError(infoEmailIncorrect);
            return;
        }

        if(!infoConfPassw.equals(infoPassw)) {
            this.text_passw.setError(infoPasswIncorrect);
            this.text_confPassw.setError(infoPasswIncorrect);
            return;
        }

        this.dao = new UsuarioDAO();
        this.usuario = new Usuario(infoName, infoEmail, infoPhone, infoUsername, infoPassw);
        if(this.dao.insertarUsuario(this.usuario)) {
            this.dao.cerrarConexion();
            Toast.makeText(this, "Usuario " +this.text_username.getText().toString()+ "creado",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al crear el Usuario " +this.text_username,Toast.LENGTH_LONG).show();
            this.dao.cerrarConexion();
        }


    }
}