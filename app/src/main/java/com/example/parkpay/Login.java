package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.UsuarioDAO;
import models.Usuario;

public class Login extends AppCompatActivity {

    private Button bt_comprobar, bt_registro;
    private EditText text_email, text_passw;
    private TextView resultadoError;
    private String errorInfo;

    private Usuario usuario;
    private UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.bt_comprobar = findViewById(R.id.id_bt_login);
        this.bt_registro = findViewById(R.id.id_bt_registarse);
        this.text_email = findViewById(R.id.id_text_login_email);
        this.text_passw = findViewById(R.id.id_text_login_passw);
        this.resultadoError = findViewById(R.id.idResultReg);

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.dao = null;
    }

    public void iniciarAplicacion(View view) {
        String datoCorreo = text_email.getText().toString();
        String datoContra = text_passw.getText().toString();
        errorInfo = "";
        resultadoError.setText("");

        // Comprueba si los campos no han sido rellenados
        if(datoCorreo.isEmpty() && datoContra.isEmpty()){
            errorInfo += "Rellena los campos";
            resultadoError.setText(errorInfo);
            return;
        }

        // Comprueba cuales son los campos vacios
        if(datoCorreo.isEmpty() || datoContra.isEmpty()){
            if(datoCorreo.isEmpty()) {
                errorInfo += "Rellena el Correo \n";
                resultadoError.setText(errorInfo);
                text_email.setText("");
            }
            if(datoContra.isEmpty()) {
                errorInfo += "Rellena la Contraseña";
                resultadoError.setText(errorInfo);
                text_passw.setText("");
            }
            return;
        }

        dao = new UsuarioDAO();
        Usuario usuario = dao.buscarUsuario(datoCorreo);
        if(usuario != null) {
            if(datoContra.equals(usuario.getPassword())) {
                dao.cerrarConexion();
                Intent intent = new Intent(this, Menu_Inicial.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else {
                dao.cerrarConexion();
                resultadoError.setText("Contraseña Incorrecta");
                text_passw.setText("");
                return;
            }

        }else{
            dao.cerrarConexion();
            resultadoError.setText("Usuario No Existe");
            return;
        }

    }

    public void irPagRegistro(View view) {
        resultadoError.setText("");
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}