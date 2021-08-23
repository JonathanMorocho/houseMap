package com.example.mapshouse;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class miPosicion implements LocationListener {
    public static double latitud;
    public static double longitud;
    public static double altitud;
    public static boolean statusGps;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitud = location.getLatitude();
        altitud = location.getAltitude();
        longitud = location.getLongitude();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        statusGps = true;
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        statusGps = false;
    }
}
