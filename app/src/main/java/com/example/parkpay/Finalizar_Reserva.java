package com.example.parkpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import dao.ReservaDAO;
import models.Parking;
import models.Reserva;
import models.Usuario;

public class Finalizar_Reserva extends AppCompatActivity {

    private Usuario usuario;
    private Reserva reserva;
    private ReservaDAO dao;
    private ImageView codigoQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finalizar_reserva);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.reserva = (Reserva) getIntent().getSerializableExtra("reserva");

        this.codigoQR = findViewById(R.id.id_img_qr);

        generarCodigoQR();

    }

    public void generarCodigoQR() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idUsuario_Fk", this.usuario.getIdUsuario());
            jsonObject.put("matricula", this.reserva.getMatricula());
            jsonObject.put("fechaReserva", this.reserva.getFechaReserva());
            jsonObject.put("inicioReserva", this.reserva.getInicioReserva());
            jsonObject.put("finalReserva", this.reserva.getFinReserva());

            // Convertimos el JSON completo a un único String de texto plano
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            // Generamos el Bitmap (Ancho: 400px, Alto: 400px)
            Bitmap bitmap = barcodeEncoder.encodeBitmap(jsonObject.toString(), BarcodeFormat.QR_CODE, 400, 400);

            this.codigoQR.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Comprimimos el bitmap en formato PNG (sin pérdida de calidad)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // Retornamos el arreglo de bytes listo para la base de datos
            this.dao = new ReservaDAO();
            if (this.dao.insertarCodigoQR(this.reserva.getIdReserva(),stream.toByteArray())) {
                Toast.makeText(this, "Codigo QR insertada", Toast.LENGTH_LONG).show();
            }
            this.dao.cerrarConexion();

        } catch (JSONException e) {
            Log.e("QR_ERROR", "Error al crear el JSON: " + e.getMessage());
        } catch (Exception e) {
            Log.e("QR_ERROR", "Error al generar el código QR: " + e.getMessage());
        }
    }

    public void volverMenu(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        intent.putExtra("usuario", this.usuario);
        startActivity(intent);
    }

    public void verListaReservas(View view) {
        Intent intent = new Intent(this, Lista_Reservas.class);
        intent.putExtra("usuario", this.usuario);
        startActivity(intent);
    }
}