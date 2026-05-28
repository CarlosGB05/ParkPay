package com.example.parkpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import models.Reserva;

public class Adapter_Reservas extends BaseAdapter {

    private ArrayList<Reserva> listaReservas;
    private Context context;
    private LayoutInflater inflater;

    public Adapter_Reservas(Context c, ArrayList<Reserva> p) {
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.listaReservas = p;
    }
    @Override
    public int getCount() {
        return this.listaReservas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = this.inflater.inflate(R.layout.listview_reservas,null);

        TextView nombre = view.findViewById(R.id.id_text_ap_nombre);
        nombre.setText(this.listaReservas.get(position).getNombreParking());

        TextView fecha = view.findViewById(R.id.id_text_ap_fecha);
        fecha.setText("Fecha reserva: " + this.listaReservas.get(position).getFechaReserva());

        TextView precio = view.findViewById(R.id.id_text_ap_precio);
        precio.setText(String.valueOf(this.listaReservas.get(position).getPrecioTotal() + "€"));

        return view;
    }
}
