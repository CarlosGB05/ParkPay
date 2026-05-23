package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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

        this.dao = new ReservaDAO();
        this.listaReservas = this.dao.buscarReservas(this.usuario.getIdUsuario());
        this.dao.cerrarConexion();

        cargarLista();
    }

    public void volverMenuInicial(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void cargarLista() {
        if (this.listaReservas.isEmpty()) {
            Toast.makeText(this, "No hay ninguna reserva", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Menu_Inicial.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
        }

        AdapPerso_Reservas adapter = new AdapPerso_Reservas(this,this.listaReservas);
        lista.setAdapter(adapter);
    }
}