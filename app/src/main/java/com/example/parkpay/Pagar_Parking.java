package com.example.parkpay;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.ReservaDAO;
import models.Parking;
import models.Reserva;
import models.Usuario;

public class Pagar_Parking extends AppCompatActivity {

    private Usuario usuario;
    private Parking parking;
    private Reserva reserva;
    private ReservaDAO dao;
    private TextView nombre, calificacion, textPrecio, error;
    private EditText matricula;
    private Switch cocheElectrico;
    private String fechaIndicada, fechaInicio, fechaFinal;
    private double precio, precioTotal;
    private boolean valorSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagar_parking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.parking = (Parking) getIntent().getSerializableExtra("parking");
        this.precio = getIntent().getDoubleExtra("precio",0);
        this.precioTotal = getIntent().getDoubleExtra("precioTotal",0);

        this.fechaIndicada = getIntent().getStringExtra("fechaIndicada");
        this.fechaInicio = getIntent().getStringExtra("horaInicial");
        this.fechaFinal = getIntent().getStringExtra("horaFinal");

        this.nombre = findViewById(R.id.id_text_parking_nombre2);
        this.nombre.setText(this.parking.getNombre());
        this.matricula = findViewById(R.id.id_text_parking_matricula);
        this.cocheElectrico = findViewById(R.id.id_switch_parking_vehiculo);
        this.textPrecio = findViewById(R.id.id_text_parking_precioTotal);
        this.error = findViewById(R.id.id_text_parking_error);
        this.error.setText("");


        this.cocheElectrico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Pagar_Parking.this.valorSwitch = true;
                } else {
                    Pagar_Parking.this.valorSwitch = false;
                }
            }
        });

        calcularPrecio();

    }

    public void indicarFechas(View view) {
        Intent intent = new Intent(this, Reservar_Calendario.class);
        intent.putExtra("usuario", this.usuario);
        intent.putExtra("parking", this.parking);
        intent.putExtra("precio", this.precio);
        startActivity(intent);
    }

    public void calcularPrecio() {
        if (this.fechaIndicada == null) {
            return;
        }

        if (this.matricula.getText() == null) {
            this.error.setText("Matrícula no indicada");
            return;
        }

        this.textPrecio.setText("Precio Total: " + this.precioTotal +"€");
    }

    public boolean validarMatricula(String matricula) {
        if (matricula == null) {
            return false;
        }

        String textoLimpio = matricula.replace("-", "").replace(" ", "").toUpperCase().trim();

        // 2. Expresión regular para el formato moderno (4 números y 3 letras válidas)
        // Excluye vocales, Ñ y Q.
        String regexModerna = "^[0-9]{4}[BCDFGHJKLMNPQRSTVWXYZ]{3}$";

        // 3. Expresión regular para el formato antiguo provincial (Ej: M1234AB, TO5555X)
        // 1 o 2 letras de provincia + 4 números + 1 o 2 letras de serie (sin Ñ, Q ni las últimas vocales en algunas series, pero más flexible)
        String regexAntigua = "^[A-Z]{1,2}[0-9]{4}[A-Z]{1,2}$";

        return textoLimpio.matches(regexModerna) || textoLimpio.matches(regexAntigua);
    }

    public void pagarParking(View view) {
        if (this.fechaIndicada == null) {
            this.error.setText("Horario no indicado");
            return;
        }

        if (this.matricula.getText().toString().isEmpty()) {
            this.error.setText("Matrícula no indicada");
            return;
        }

        if (validarMatricula(this.matricula.getText().toString())) {
            Toast.makeText(this, "Matrícula válida", Toast.LENGTH_SHORT).show();
        }

        this.reserva = new Reserva(this.usuario.getIdUsuario(), this.parking.getNombre(),this.parking.getDireccion(),
                this.parking.getCalificacion(),this.fechaIndicada,this.fechaInicio,this.fechaFinal,this.precioTotal,
                this.matricula.getText().toString(),this.valorSwitch);
        this.dao = new ReservaDAO();
        if (this.dao.insertarReserva(this.reserva)) {
            this.dao.cerrarConexion();
            Toast.makeText(this,"Reserva insertado BBDD", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Buscar_Parking.class);
            intent.putExtra("usuario", this.usuario);
            startActivity(intent);
        }
    }

    public void cancelarPago(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        intent.putExtra("usuario", this.usuario);
        startActivity(intent);
    }

}