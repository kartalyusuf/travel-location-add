package com.kartal.travelbookjava;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //MainActivityJava içinde yaptığımız item kısmı "info" oradan geliyor.
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.kartal.travelbookjava",MODE_PRIVATE);
                    

                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                }
            };

            //eğer izin verilmediyse
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //izin isteme
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                //eğer zaten izin verildiyse
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastLocation != null) {

                    LatLng LastUserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LastUserLocation, 15));
                }


            }
        }else {
            //SqLite Data
        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

      locationListener = new LocationListener() {
          @Override
          public void onLocationChanged(@NonNull Location location) {

              LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

          }
      };

      //eğer izin verilmediyse
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

          //izin isteme
          ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

          //eğer zaten izin verildiyse
      }else {
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

          Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

          if (lastLocation != null) {

              LatLng LastUserLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LastUserLocation,15));
          }
      }


    }


    //İzinler verildikten sonra yapılacak kontroller buradan yapılıyor
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if (grantResults.length > 0 ) {
            if (requestCode == 1) {

                //izin verildiyse
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    
                }
            }
        }
    }
}