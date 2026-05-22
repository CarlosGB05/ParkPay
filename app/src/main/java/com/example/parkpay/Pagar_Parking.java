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

import models.Parking;
import models.Reserva;
import models.Usuario;

public class Pagar_Parking extends AppCompatActivity {

    private Usuario usuario;
    private Parking parking;
    private Reserva reserva;
    private TextView nombre, calificacion, Textprecio, error;
    private EditText matricula;
    private Switch cocheElectrico;
    private String fechaIndicada, valorSwitch;
    private double precio, precioTotal;

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

        this.nombre = findViewById(R.id.id_text_parking_nombre2);
        this.nombre.setText(this.parking.getNombre());
        this.matricula = findViewById(R.id.id_text_parking_matricula);
        this.cocheElectrico = findViewById(R.id.id_switch_parking_vehiculo);
        this.Textprecio = findViewById(R.id.id_text_parking_precioTotal);
        this.error = findViewById(R.id.id_text_parking_error);
        this.error.setText("");
        this.fechaIndicada = "";

        this.cocheElectrico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Pagar_Parking.this.error.setText("Es electrico");
                } else {
                    Pagar_Parking.this.error.setText("No es electrico");
                }
            }
        });

    }

    public void indicarFechas(View view) {
        Intent intent = new Intent(this, Reservar_Calendario.class);
        intent.putExtra("usuario", this.usuario);
        intent.putExtra("parking", this.parking);
        intent.putExtra("precio", this.precio);
        startActivity(intent);
    }

}