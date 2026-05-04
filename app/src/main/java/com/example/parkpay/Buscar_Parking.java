package com.example.parkpay;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkpay.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Buscar_Parking extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private AutoCompleteTextView autoComplete;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_parking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        mapFragment.getMapAsync(this);

        // Inicializa Places con la clave de API que creaste en image_641463.png
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDW-N6trkPPa5AyHYfSha3s_9OYCXYrtsI");
        }
        // Crear un cliente de Places
        this.placesClient = Places.createClient(this);

        // Lista donde guardaremos los nombres que nos devuelva Google
        ArrayList<String> listaSugerencias = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaSugerencias);

        this.autoComplete = findViewById(R.id.id_text_buscar_parking);
        this.autoComplete.setAdapter(adapter);

        this.autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2 && autoComplete.isFocused()) { // Solo busca si ha escrito más de 2 letras
                    obtenerPredicciones(s.toString(), adapter, listaSugerencias);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        this.autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 1. Extraer el texto de la opción seleccionada (ej: "Yepes, Toledo, España")
                String lugarSeleccionado = parent.getItemAtPosition(position).toString();

                // 2. Notificar al usuario o hacer log (Opcional)
                Toast.makeText(Buscar_Parking.this, "Cargando: " + lugarSeleccionado, Toast.LENGTH_SHORT).show();

                // 3. ¡LA ACCIÓN CLAVE!: Llamar a la función que mueve el mapa
                geolocalizarYFocalizar(lugarSeleccionado);

                // 4. (Opcional) Ocultar el teclado para ver mejor el mapa
                autoComplete.dismissDropDown(); // Fuerza el cierre de la lista visualmente
                autoComplete.clearFocus();      // Quita el cursor del buscador
            }
        });
    }

    public void volverMenu(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        startActivity(intent);
    }

    private void geolocalizarYFocalizar(String nombreLugar) {
        if (mMap == null) {
            Toast.makeText(this, "El mapa aún no está listo", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            // Buscamos el lugar (devuelve una lista de direcciones)
            List<Address> lista = geocoder.getFromLocationName(nombreLugar, 1);

            if (lista != null && !lista.isEmpty()) {
                Address address = lista.get(0);
                LatLng ubicacion = new LatLng(address.getLatitude(), address.getLongitude());

                // Movemos la cámara al lugar encontrado
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));

                // Añadimos un marcador
                mMap.clear(); // Limpia marcadores anteriores
                mMap.addMarker(new MarkerOptions().position(ubicacion).title(nombreLugar));
            }
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo encontrar el lugar: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void obtenerPredicciones(String query, ArrayAdapter<String> adapter, ArrayList<String> lista) {
        Log.d("PRUEBA", "Petición enviada para: " + query);

        // Si por alguna razón sigue siendo null, salimos del método sin romper la app
        if (placesClient == null) {
            Toast.makeText(this, "Places no ha sido inicializado",Toast.LENGTH_LONG).show();
            return;
        }

        // Definimos el área de búsqueda o sesión (opcional pero recomendado)
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Creamos la petición
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .setCountries("ES") // Filtra solo para España (puedes quitarlo)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            lista.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                lista.add(prediction.getFullText(null).toString());
            }
            adapter.notifyDataSetChanged(); // Refresca la lista visualmente

            // Cambia estas líneas al final del SuccessListener:
            adapter.clear();
            lista.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                lista.add(prediction.getFullText(null).toString());
            }
            adapter.addAll(lista); // Usa addAll para que el adaptador gestione la lista internamente
            adapter.getFilter().filter(null); // ESTA LÍNEA ES CLAVE: Desactiva el filtro interno
            adapter.notifyDataSetChanged();
            this.autoComplete.showDropDown();
        }).addOnFailureListener(exception -> {
            Toast.makeText(this, "No se pudo obtener el lugar",Toast.LENGTH_LONG).show();
            Log.e("PLACES_ERROR", "Causa: " + exception.getMessage());
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("PLACES_ERROR", "Código de estado: " + apiException.getStatusCode());
            }
        });
    }

    private void ocultarTeclado() {
        // 1. Obtener el servicio del sistema encargado del teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 2. Obtener la vista que tiene el foco actualmente
        View view = this.getCurrentFocus();

        // 3. Si hay una vista con el foco, pedir al servicio que oculte el teclado
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}