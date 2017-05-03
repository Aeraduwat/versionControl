package com.ejemplo4.test4;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker puntero;
    private Marker pos1;
    double latitude = 0.0;
    double longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //tras crear el metodo mi Ubicacion se ingresa el metodo que actualiza la ubicacion
        MiUbicacion();
    }



    private void AgregarMarcador(double latitude, double longitude) {
        LatLng coordenadas = new LatLng(latitude, longitude);
        //centra la camara en la posicion actual.
        CameraUpdate Ubicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (puntero != null) puntero.remove();
        puntero = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi Ubicacion")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(Ubicacion);
    }

    //obtener latitud y longitud, pero con validacion
    //PARA EVITAR QUE LA APP SE CIERRE AL INICIAR, POR TENER VALORES **NULOS**

    private void actualizarUbicacion(Location Location) {
        if (Location != null) {
            latitude = Location.getLatitude();
            longitude = Location.getLongitude();
            AgregarMarcador(latitude, longitude);
        }
    }

    //SE CREA EL METODO LOCATION LISTENER, EL CUAL ESTA PENDIENTE DE CUALQUIER CAMBIO DE UBICACION

    LocationListener locatioListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /*se crea una clase para solicitar la ultima ubicacion conocida, utilizando LocationManager, que
     provee de servicios de posicionamiento
      */
    private void MiUbicacion() {
        //permiso necesario para poder actualizar la ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        //actualizacion de la ubicacion por gps, con dilay de 15 segundos
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locatioListener);

    }
}
