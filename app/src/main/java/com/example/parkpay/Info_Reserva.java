package com.example.parkpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.ReservaDAO;
import models.Reserva;
import models.Usuario;

public class Info_Reserva extends AppCompatActivity {

    private Usuario usuario;
    private Reserva reserva;
    private ReservaDAO dao;
    private ImageView codigoQR;
    private TextView nombreParking, ubicacionParking, fechaReserva, matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_reserva);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.reserva = (Reserva) getIntent().getSerializableExtra("reserva");

        this.codigoQR = findViewById(R.id.id_img_qr2);
        this.nombreParking = findViewById(R.id.id_text_reserva_infoNombre);
        this.ubicacionParking = findViewById(R.id.id_text_reserva_infoUbicacion);
        this.fechaReserva = findViewById(R.id.id_text_reserva_infoFecha);
        this.matricula = findViewById(R.id.id_text_reserva_infoMatricula);

        cargarCodigoQR();
        cargarInfo();
    }

    public void cargarCodigoQR() {
        this.dao = new ReservaDAO();
        byte[] img = this.dao.buscarCodigoQR(this.reserva.getIdReserva());
        this.dao.cerrarConexion();
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            this.codigoQR.setImageBitmap(bitmap);
        }
    }

    public void cargarInfo() {
        this.nombreParking.setText(this.reserva.getNombreParking());
        this.ubicacionParking.setText(this.reserva.getUbicacionParking());
        this.fechaReserva.setText(this.reserva.getFechaReserva() + " " + this.reserva.getInicioReserva() + " - " + this.reserva.getFinReserva());
        this.matricula.setText(this.reserva.getMatricula());
    }

    public void volverLista(View view) {
        Intent intent = new Intent(this, Lista_Reservas.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

}