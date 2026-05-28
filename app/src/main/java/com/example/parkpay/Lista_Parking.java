package com.example.parkpay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Parking;
import models.Usuario;

public class Lista_Parking extends AppCompatActivity {

    private Usuario usuario;
    private LatLng ubicacion;
    private int ratio;
    private PlacesClient placesClient;
    private ArrayList<Parking> listaParkings;
    private ListView lista;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_parking);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDW-N6trkPPa5AyHYfSha3s_9OYCXYrtsI");
        }
        placesClient = Places.createClient(this);

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.ubicacion = getIntent().getParcelableExtra("ubicacion");
        this.ratio = getIntent().getIntExtra("ratio",0);
        this.lista = findViewById(R.id.idLista);
        this.error = findViewById(R.id.id_listview_error2);
        this.error.setText("");

        if (this.ubicacion != null) {
            buscarParkingsConVolley(this.ubicacion, this.ratio);
        }

        this.lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Parking p = Lista_Parking.this.listaParkings.get(position);
                Intent intent = new Intent(Lista_Parking.this, Info_Parking.class);
                intent.putExtra("usuario", Lista_Parking.this.usuario);
                intent.putExtra("parking", p);
                intent.putExtra("lista",Lista_Parking.this.listaParkings);
                startActivity(intent);
            }
        });

    }


    private void buscarParkingsConVolley(LatLng ubicacion, int radioKm) {

        this.listaParkings = new ArrayList<>();

        // 1. Preparamos los parámetros
        String apiKey = "AIzaSyCES2UlSpC1bLtf39vPm3XA-_HdDaYSVEE";
        int radioMetros = radioKm * 1000;
        String location = ubicacion.latitude + "," + ubicacion.longitude;

        // 2. Construimos la URL de Nearby Search (Web Service)
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + location +
                "&radius=" + radioMetros +
                "&type=parking" +
                "&key=" + apiKey;

        // 3. Creamos la cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(this);

        // 4. Realizamos la petición JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        listaParkings.clear(); // Limpiamos para evitar duplicados

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);

                            // Extraer datos básicos
                            String nombre = obj.getString("name");
                            String direccion = obj.optString("vicinity", "Dirección no disponible");
                            double rating = obj.optDouble("rating", 0.0);
                            String placeId = obj.getString("place_id");

                            // Extraer coordenadas (están dentro de geometry -> location)
                            JSONObject geometry = obj.getJSONObject("geometry");
                            JSONObject location2 = geometry.getJSONObject("location");
                            double lat = location2.getDouble("lat");
                            double lng = location2.getDouble("lng");

                            // Crear el objeto y añadirlo a la lista
                            Parking p = new Parking(nombre, direccion, lat, lng, rating, placeId);
                            listaParkings.add(p);
                        }

                        // ¡IMPORTANTE! Aquí es donde notificas a tu ListView que ya tiene datos
                        if (this.listaParkings.isEmpty()) {
                            this.error.setText("No se encontraron ningún Parking por esa zona");
                            return;
                        }
                        Adapter_Parking adapter = new Adapter_Parking(this,this.listaParkings);
                        lista.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("API_VOLLEY", "Error en la petición: " + error.getMessage())
        );

        // 5. Añadimos la petición a la cola
        queue.add(jsonObjectRequest);
    }

    public void volverBuscarParking(View view) {
        Intent intent = new Intent(this, Buscar_Parking.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }



}