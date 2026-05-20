package com.example.parkpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import models.Parking;

public class AdapterPersonalizado extends BaseAdapter {

    private ArrayList<Parking> listaParkings;
    private Context context;
    private LayoutInflater inflater;

    public AdapterPersonalizado(Context c, ArrayList<Parking> p) {
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.listaParkings = p;
    }
    @Override
    public int getCount() {
        return this.listaParkings.size();
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
        view = this.inflater.inflate(R.layout.listview2,null);

        TextView nombre = view.findViewById(R.id.id_text_parking_nombre);
        nombre.setText(this.listaParkings.get(position).getNombre());

        TextView direccion = view.findViewById(R.id.id_text_listview_direccion2);
        direccion.setText(this.listaParkings.get(position).getDireccion());

//        RatingBar reseña = view.findViewById(R.id.id_text_listview_reseña);
//        reseña.setRating((float) this.listaParkings.get(position).getCalificacion());
        TextView calificacion = view.findViewById(R.id.id_text_listview_resena2);
        calificacion.setText(String.valueOf(this.listaParkings.get(position).getCalificacion()));

        return view;
    }
}
