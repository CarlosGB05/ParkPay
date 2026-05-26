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

public class Editar_Usuario extends AppCompatActivity {

    private EditText update_Name, update_Phone, update_Dni,
            update_Passw, update_ConfPassw;
    private Usuario usuario;
    private UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        this.update_Name = findViewById(R.id.id_text_update_name);
        this.update_Phone = findViewById(R.id.id_text_update_phone);
        this.update_Dni = findViewById(R.id.id_text_update_dni);
        this.update_Passw = findViewById(R.id.id_text_update_passw);
        this.update_ConfPassw = findViewById(R.id.id_text_update_confPassw);

        añadirInfoEditText();
    }

    public void añadirInfoEditText() {
        this.update_Name.setText(this.usuario.getNombreCompleto());
        this.update_Phone.setText(String.valueOf(this.usuario.getTelefono()));
        if (this.usuario.getDni() == null) {
            this.update_Dni.setText("");
        } else {
            this.update_Dni.setText(this.usuario.getDni());
        }
    }

    public void cancelarActualizacion(View view) {
        Intent intent = new Intent(this, Info_Usuario.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void actualizarUsuario(View view) {
        String infoName = this.update_Name.getText().toString();
        String valorTf = this.update_Phone.getText().toString();
        Integer infoPhone;
        if(valorTf.isEmpty()) {
            infoPhone = 0;
        } else {
            infoPhone = Integer.parseInt(valorTf);
        }
        String infoDni = this.update_Dni.getText().toString();
        String infoPassw = this.update_Passw.getText().toString();
        String infoConfPassw = this.update_ConfPassw.getText().toString();
        String infoEmpty = "Rellena todos los datos";
        String infoNameLongitud = "Máx 20 caracteres";
        String infoTelefLongitud = "Máx 9 Números";
        String infoPasswIncorrect = "No coinciden";
        String infoIncorrectDNI = "DNI no valido";
        String infoDNILongitud = "Máx 9 caracteres";

        if ((infoName.isEmpty() && infoPhone == 0 && infoDni.isEmpty() &&
                infoPassw.isEmpty() && infoConfPassw.isEmpty()) ||
                (infoName.isEmpty() || infoPhone == 0 ||
                        infoDni.isEmpty() || infoPassw.isEmpty() || infoConfPassw.isEmpty())) {
            this.update_Name.setError(infoEmpty);
            this.update_Phone.setError(infoEmpty);
            this.update_Dni.setError(infoEmpty);
            this.update_Passw.setError(infoEmpty);
            this.update_ConfPassw.setError(infoEmpty);
            return;
        }

        if(infoName.length() > 25) {
            this.update_Name.setError(infoNameLongitud);
            return;
        }

        if(infoPhone.toString().length() < 9 || infoPhone.toString().length() > 9) {
            this.update_Phone.setError(infoTelefLongitud);
            return;
        }

        if (infoDni.toString().length() < 9 || infoDni.toString().length() > 9) {
            this.update_Dni.setError(infoDNILongitud);
            return;
        }

        if (!this.validarDNI(infoDni)) {
            this.update_Dni.setError(infoIncorrectDNI);
            return;
        }

        if(!infoConfPassw.equals(infoPassw)) {
            this.update_Passw.setError(infoPasswIncorrect);
            this.update_ConfPassw.setError(infoPasswIncorrect);
            return;
        }

        this.dao = new UsuarioDAO();
        // Error: Desaparece el Id
        Usuario newUsuario = new Usuario(infoName, this.usuario.getEmail(), infoPhone, infoDni, infoPassw);
        if(this.dao.actualizarUsuario(newUsuario)) {
            this.usuario = this.dao.buscarUsuario(newUsuario.getEmail());
            this.dao.cerrarConexion();
            Intent intent = new Intent(this, Info_Usuario.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
        } else {
            this.dao.cerrarConexion();
        }

    }

    private boolean validarDNI(String dni) {
        if (dni == null) {
            return false;
        }

        String cleanedDni = dni.trim().replace("-", "").toUpperCase();

        if (!cleanedDni.matches("^[XYZ0-9][0-9]{7}[A-Z]$")) {
            return false;
        }

        try {

            String numeroStr = cleanedDni.substring(0, 8);
            char primerCaracter = numeroStr.charAt(0);

            if (primerCaracter == 'X') {
                numeroStr = numeroStr.replace('X', '0');
            } else if (primerCaracter == 'Y') {
                numeroStr = numeroStr.replace('Y', '1');
            } else if (primerCaracter == 'Z') {
                numeroStr = numeroStr.replace('Z', '2');
            }

            char letraIntroducida = cleanedDni.charAt(8);

            String letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE";
            int numero = Integer.parseInt(numeroStr);
            int resto = numero % 23;
            char letraCorrecta = letrasDni.charAt(resto);

            return letraIntroducida == letraCorrecta;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}