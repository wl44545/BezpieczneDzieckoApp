package com.example.bezpiecznedziecko.parent.maps;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class parentMap extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_map);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_login));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        float longitude = sharedPref.getFloat(getString(R.string.shared_preferences_longitude), (float) 0.0);
        float latitude = sharedPref.getFloat(getString(R.string.shared_preferences_latitude), (float) 0.0);
        LatLng latlng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Ja"));

        /*mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(schedule_latitude), Double.parseDouble(schedule_longitude)))
                .radius(Double.parseDouble(schedule_radius))
                .strokeColor(Color.RED)
                .fillColor(Color.argb(32,255,0,0)));*/

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, parentMain.class);
        startActivity(intent);
    }


}