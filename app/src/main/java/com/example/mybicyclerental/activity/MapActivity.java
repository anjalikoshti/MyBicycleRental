package com.example.mybicyclerental.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mybicyclerental.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;

        LatLng sydney = new LatLng(23.02579, 72.58727);

        googleMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}