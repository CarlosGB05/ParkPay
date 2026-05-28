package com.example.parkpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;

import dao.ReservaDAO;
import models.Reserva;
import models.Usuario;

public class Info_Reserva extends AppCompatActivity {

    private Usuario usuario;
    private Reserva reserva;
    private ReservaDAO dao;
    private ImageView codigoQR, verQr;
    private TextView nombreParking, ubicacionParking, fechaReserva, matricula;
    private AlertDialog dialog;

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

        this.codigoQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verCodigoQr();
            }
        });
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

    public void verCodigoQr() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_codigo_qr, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(Info_Reserva.this);
        builder.setView(dialogView);

        builder.setCancelable(true);

        Info_Reserva.this.dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        this.verQr = dialogView.findViewById(R.id.id_dialog_qr);
        this.dao = new ReservaDAO();
        byte[] img = this.dao.buscarCodigoQR(this.reserva.getIdReserva());
        this.dao.cerrarConexion();
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            this.verQr.setImageBitmap(bitmap);
        }
    }

    public void eliminarReserva(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(Info_Reserva.this);
        builder.setView(dialogView);

        builder.setCancelable(true);

        Info_Reserva.this.dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            dialog.getWindow().setGravity(Gravity.CENTER);
        }

    }

    public void aceptarEliminar(View view) {
        this.dao = new ReservaDAO();
        if (this.dao.eliminarCodigoQR(this.reserva.getIdReserva())) {
            if (this.dao.eliminarReserva(this.reserva.getIdReserva())) {
                Toast.makeText(this, "Reserva Eliminada", Toast.LENGTH_LONG).show();
                this.dialog.dismiss();
                this.dao.cerrarConexion();
                Intent intent = new Intent(this, Lista_Reservas.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        }
        this.dao.cerrarConexion();
    }

    public void cancelarEliminar(View view) {
        this.dialog.dismiss();
    }

}