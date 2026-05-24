package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import dao.ReservaDAO;
import models.Parking;
import models.Reserva;
import models.Usuario;

public class Lista_Reservas extends AppCompatActivity {

    private Usuario usuario;
    private ReservaDAO dao;
    private ListView lista;
    private TextView error;
    private ArrayList<Reserva> listaReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_reservas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        this.lista = findViewById(R.id.id_listview_reservas);
        this.error = findViewById(R.id.id_listview_error);
        this.error.setText("");

        this.dao = new ReservaDAO();
        this.listaReservas = this.dao.buscarListaReservas(this.usuario.getIdUsuario());
        this.dao.cerrarConexion();

        cargarLista();

        this.lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reserva r = Lista_Reservas.this.listaReservas.get(position);
                Intent intent = new Intent(Lista_Reservas.this, Info_Reserva.class);
                intent.putExtra("usuario", Lista_Reservas.this.usuario);
                intent.putExtra("reserva", r);
                startActivity(intent);
            }
        });
    }

    public void volverMenuInicial(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void cargarLista() {
        if (this.listaReservas.isEmpty()) {
            this.error.setText("No tienes ninguna reserva realizada");
        }

        AdapPerso_Reservas adapter = new AdapPerso_Reservas(this,this.listaReservas);
        lista.setAdapter(adapter);
    }
}