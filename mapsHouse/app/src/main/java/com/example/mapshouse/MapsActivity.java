package com.example.mapshouse;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapshouse.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String longitud, latitud;
    private Button bnEliminarM, bnMarcas, btnFavorito;
    SharedPreferences sharedPreferences;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bnEliminarM = findViewById(R.id.bnLimpiarMarcas);
        bnMarcas = findViewById(R.id.bnVerMarcas);
        btnFavorito = findViewById(R.id.btnfavoritos);

        bnEliminarM.setOnClickListener(this);
        btnFavorito.setOnClickListener(this);
        bnMarcas.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //obtengo los datos de la logitud y latitud
        longitud = getIntent().getStringExtra("longitud");
        latitud = getIntent().getStringExtra("latitud");

        //convierto a double los datos

        double lon = Double.parseDouble(longitud);
        double lat = Double.parseDouble(latitud);
        mMap = googleMap;

        sharedPreferences=getSharedPreferences("MyPreferences1", Context.MODE_PRIVATE);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación"));

       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
       // CameraUpdate zoomCam = CameraUpdateFactory.zoomTo(16);

        //mMap.animateCamera(zoomCam);

        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("ubicacion"));
        Toast.makeText(this, "" + marker.getPosition(), Toast.LENGTH_LONG).show();
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        GuardarPreferencia(latLng);
    }

    public void GuardarPreferencia(LatLng latLng){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitud", (float) latLng.latitude);
        editor.putFloat("longitud", (float) latLng.longitude);
        editor.commit();
    }

    public void CargarPreferencia(){
        double lat = sharedPreferences.getFloat("latitud", 0);
        double log = sharedPreferences.getFloat("longitud", 0);

        if(lat != 0){
            LatLng puntoPref = new LatLng(lat, log);
            marker = mMap.addMarker(new MarkerOptions().position(puntoPref).title("ubicacion"));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(puntoPref));

        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("No tiene ningun sitio favorito");
            alert.setPositiveButton("OK", null);
            alert.create().show();
        }
        Toast.makeText(this, "Cargas preferencias", Toast.LENGTH_LONG).show();

    }
    public void  eliminarMarcas(){
        mMap.clear();
        Toast.makeText(this, "Marcas Eliminadas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(btnFavorito == v){
            CargarPreferencia();
            Toast.makeText(this, "Preferencia cargar", Toast.LENGTH_LONG).show();
            //marker.remove();
        }else if (bnEliminarM == v){
            eliminarMarcas();
        }

    }


}