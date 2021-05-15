package com.example.bezpiecznedziecko.child.maps;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.child.main.childMain;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class childMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button tmp_btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tmp_btn_refresh = (Button)findViewById(R.id.tmp_btn_refresh);
        tmp_btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                onMapReady(mMap);
            }
        });

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
                .center(latlng)
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));*/

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}