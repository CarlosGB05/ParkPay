package com.example.parkpay;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.example.parkpay.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Usuario;

public class Buscar_Parking extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private AutoCompleteTextView autoComplete;
    private PlacesClient placesClient;
    private Slider barrita;
    private TextView valorBarrita;
    private LatLng ubicacionIndicado;
    private int ratioBusqueda;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_parking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        mapFragment.getMapAsync(this);

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        this.barrita = findViewById(R.id.id_seekBar);
        this.valorBarrita = findViewById(R.id.id_text_valor_seekbar);
        this.valorBarrita.setText("0Km");

        this.barrita.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                ratioBusqueda = (int) value;
                valorBarrita.setText(String.valueOf((int) value) + "Km");
            }
        });

        // Inicializa Places con la clave de API
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

                // 2. ¡LA ACCIÓN CLAVE!: Llamar a la función que mueve el mapa
                geolocalizarYFocalizar(lugarSeleccionado);

                // 3. (Opcional) Ocultar el teclado para ver mejor el mapa
                autoComplete.dismissDropDown(); // Fuerza el cierre de la lista visualmente
                autoComplete.clearFocus();      // Quita el cursor del buscador

            }
        });
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
                this.ubicacionIndicado = new LatLng(address.getLatitude(), address.getLongitude());

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        obtenerUbicacionActual();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario aceptó los permisos en la ventana flotante, obtener la ubicación
                obtenerUbicacionActual();
            }
        }
    }

    private void obtenerUbicacionActual() {
        // 1. Verificar si el usuario ya dio los permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Si no los tiene, solicitarlos al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // 2. Activar el botón azul nativo de "Mi Ubicación" en el mapa (Opcional)
        mMap.setMyLocationEnabled(true);

        // 3. Obtener la última ubicación conocida por el dispositivo
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Si se logró obtener la ubicación con éxito
                        if (location != null) {
                            // Crear el objeto LatLng con los datos reales del GPS
                            LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                            // Añadir el marcador en tu posición actual
                            mMap.addMarker(new MarkerOptions()
                                    .position(miUbicacion)
                                    .title("Estoy aquí"));

                            // Mover la cámara hacia tu posición con un nivel de zoom adecuado (ej. 15f)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 15f));
                        }
                    }
                });
    }

    private void obtenerPredicciones(String query, ArrayAdapter<String> adapter, ArrayList<String> lista) {
        System.out.println("PRUEBA: Petición enviada para: " + query);

        // Si por alguna razon sigue siendo null, salimos del metodo sin romper la app
        if (placesClient == null) {
            System.out.println("Places no ha sido inicializado");
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
            System.out.println("PLACES_ERROR: Causa: " + exception.getMessage());
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                System.out.println("PLACES_ERROR: Código de estado: " + apiException.getStatusCode());
            }
        });
    }

    public void volverMenu(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void listaParkings(View view) {
        if (this.ubicacionIndicado == null) {
            Toast.makeText(this, "Indica el lugar a buscar", Toast.LENGTH_LONG).show();
            return;
        }

        if (this.ratioBusqueda <= 0) {
            Toast.makeText(this, "Indica un ratio MAYOR a 0", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, Lista_Parking.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("ubicacion", this.ubicacionIndicado);
        intent.putExtra("ratio", this.ratioBusqueda);
        startActivity(intent);
    }

}