package com.example.parkpay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import models.Parking;
import models.Usuario;

public class Info_Parking extends AppCompatActivity {

    private Usuario usuario;
    private Parking parking;
    private TextView nombre, direccion, calificacion, Textprecio, error;
    private double precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_parking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.parking = (Parking) getIntent().getSerializableExtra("parking");

        this.nombre = findViewById(R.id.id_text_parking_nombre);
        this.nombre.setText(this.parking.getNombre());
        this.calificacion = findViewById(R.id.id_text_parking_califi);
        this.calificacion.setText(String.valueOf(this.parking.getCalificacion()));
        this.direccion = findViewById(R.id.id_text_parking_ubicacion);
        this.direccion.setText(this.parking.getDireccion());
        this.Textprecio = findViewById(R.id.id_text_parking_precio);
        this.error = findViewById(R.id.id_text_parking_error);
        this.error.setText("");

        precioParking();
    }

    private void precioParking() {
        double valor = this.parking.getCalificacion();

        if (valor >= 0 && valor <= 1.9) {
            this.precio = 1.5;
            this.Textprecio.setText("Precio: "+this.precio+" €/Hora");
            return;
        }

        if (valor >= 2 && valor <= 3.9) {
            this.precio = 3.5;
            this.Textprecio.setText("Precio: "+this.precio+" €/Hora");
            return;
        }

        if (valor >= 4 && valor < 4.5) {
            this.precio = 4.5;
            this.Textprecio.setText("Precio: "+this.precio+" €/Hora");
            return;
        }

        if (valor >= 4.5) {
            this.precio = 6;
            this.Textprecio.setText("Precio: "+this.precio+" €/Hora");
            return;
        }
    }

    public void comprobarPago(View view) {
        this.error.setText("* Debe indicar una fecha para continuar");
    }

    public void cancelarPago(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        intent.putExtra("usuario",this.usuario);
        startActivity(intent);
    }


}