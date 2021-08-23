package com.example.mapshouse.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapshouse.MapsActivity;
import com.example.mapshouse.R;
import com.example.mapshouse.databinding.FragmentHomeBinding;
import com.example.mapshouse.miPosicion;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private EditText txtLongitud;
    private EditText txtLatitud;
    private EditText txtAltitud;
    private Button bnUbicacion;
    private Button bnMapa;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtLatitud = root.findViewById(R.id.txtlatitud);
        txtLongitud = root.findViewById(R.id.txtlongitud);
        txtAltitud = root.findViewById(R.id.txtaltitud);
        bnUbicacion = root.findViewById(R.id.bnUbicacion);
        bnMapa = root.findViewById(R.id.bnMapa);

        bnUbicacion.setOnClickListener(this);
        bnMapa.setOnClickListener(this);
        Permisos();

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void Permisos(){
        int permisoLocalizacion = ContextCompat.checkSelfPermission((Activity)getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoInternet = ContextCompat.checkSelfPermission((Activity)getContext(), Manifest.permission.CAMERA);

        if(permisoLocalizacion != PackageManager.PERMISSION_GRANTED && permisoInternet != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA }, 1000);
        }

        LocationManager objLocation=null;
        LocationListener objLocListener;

        objLocation=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        objLocListener= (LocationListener) new miPosicion();
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,objLocListener);

        if(objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            txtLongitud.setText(miPosicion.longitud + "");
            txtLatitud.setText(miPosicion.latitud + "");
            txtAltitud.setText(miPosicion.altitud+"");
        }
    }


    //metodo para verificar que los campos esten llenos

    public void verificar(){
        if (!txtAltitud.getText().toString().equals("")|| !txtLongitud.getText().toString().equals("") || !txtLatitud.getText().toString().equals("")){
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("latitud", txtLatitud.getText().toString());
            intent.putExtra("longitud", txtLongitud.getText().toString());
            intent.putExtra("altitud", txtAltitud.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Los campo no estan llenos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == bnUbicacion){
            Permisos();

        }
        if (v == bnMapa){
            verificar();
        }
    }
}
