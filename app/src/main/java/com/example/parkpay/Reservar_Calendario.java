package com.example.parkpay;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import models.Parking;
import models.Usuario;

public class Reservar_Calendario extends AppCompatActivity {
    private Usuario usuario;
    private Parking parking;
    private TextView fecha, horaInicio, horaFinal, error;
    private ListView listaInicio, listaFinal;
    private String[] listaHoras;
    private double precio;
    private CalendarView calendario;
    private Button btCalendario, aceptar, cancelar;
    private AlertDialog dialog;
    private String dia, mes, anio, fechaIndicada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservar_calendario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.parking = (Parking) getIntent().getSerializableExtra("parking");
        this.precio = getIntent().getDoubleExtra("precio",0);

        this.fecha = findViewById(R.id.id_text_calendario_fecha);
        this.fecha.setText("");
        this.horaInicio = findViewById(R.id.id_text_calendario_fechaInicio);
        this.horaFinal = findViewById(R.id.id_text_calendario_fechaFinal);
        this.listaInicio = findViewById(R.id.id_text_calendario_ListviewInicio);
        this.listaFinal = findViewById(R.id.id_text_calendario_ListviewFinal);
        this.error = findViewById(R.id.id_text_calendario_error);
        this.error.setText("");
        this.dia = "";
        this.mes = "";
        this.anio = "";
        this.fechaIndicada = "";

        this.listaHoras = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00",
                "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> adapterInicio = new ArrayAdapter<>(Reservar_Calendario.this, android.R.layout.simple_list_item_1, this.listaHoras);
        this.listaInicio.setAdapter(adapterInicio);
        ArrayAdapter<String> adapterFinal = new ArrayAdapter<>(Reservar_Calendario.this, android.R.layout.simple_list_item_1, this.listaHoras);
        this.listaFinal.setAdapter(adapterFinal);

        this.listaInicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reservar_Calendario.this.horaInicio.setText(Reservar_Calendario.this.listaHoras[position]);
            }
        });

        this.listaFinal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reservar_Calendario.this.horaFinal.setText(Reservar_Calendario.this.listaHoras[position]);
            }
        });
    }

    public void cancelarFechas(View view) {
        Intent intent = new Intent(this, Reservar_Calendario.class);
        intent.putExtra("usuario", this.usuario);
        intent.putExtra("parking", this.parking);
        intent.putExtra("precio", this.precio);
        startActivity(intent);
    }

    public void verCalendario(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_calendario, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(Reservar_Calendario.this);
        builder.setView(dialogView);

        builder.setCancelable(false);

        Reservar_Calendario.this.dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        this.calendario = dialogView.findViewById(R.id.id_dialog_calendario);
        this.aceptar = dialogView.findViewById(R.id.id_btn_dialog_calendarAccept);
        this.cancelar = dialogView.findViewById(R.id.id_btn_dialog_calendarCancel);

        this.calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Reservar_Calendario.this.dia = String.valueOf(dayOfMonth);
                Reservar_Calendario.this.mes = String.valueOf((month + 1));
                Reservar_Calendario.this.anio = String.valueOf(year);
            }
        });
    }

    public void aceptarCalendario(View view) {
        String fecha = "";
        if (this.dia.isEmpty() || this.mes.isEmpty() || this.anio.isEmpty()) {
            Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_LONG).show();
            return;
        }
        this.fechaIndicada = this.dia + " / " + this.mes + " / " + this.anio;
        fecha = "Fecha indicada:   " + this.dia + " / " + this.mes + " / " + this.anio;
        this.fecha.setText(fecha);
        this.dialog.dismiss();
    }

    public void cancelarCalendario(View view) {
        this.dialog.dismiss();
    }


}