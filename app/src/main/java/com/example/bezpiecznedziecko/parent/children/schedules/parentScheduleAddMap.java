package com.example.bezpiecznedziecko.parent.children.schedules;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.parent.children.parentChildProfile;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;

public class parentScheduleAddMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText edt_radius;
    Button btn_save, btn_radius;
    String map_latitude, map_longitude, map_radius, child;
    float latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule_add_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        child = intent.getStringExtra("login");

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        longitude = sharedPref.getFloat(getString(R.string.shared_preferences_longitude), (float) 0.0);
        latitude = sharedPref.getFloat(getString(R.string.shared_preferences_latitude), (float) 0.0);
        map_latitude = String.valueOf(latitude);
        map_longitude = String.valueOf(longitude);

        edt_radius = (EditText)findViewById(R.id.edt_radius);
        btn_radius = (Button)findViewById(R.id.btn_radius);
        btn_radius.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                mMap.clear();
                map_radius = edt_radius.getText().toString();
                LatLng latlng = new LatLng(Double.parseDouble(map_latitude), Double.parseDouble(map_longitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                mMap.addMarker(markerOptions);
                mMap.addCircle(new CircleOptions()
                        .center(latlng)
                        .radius(Double.parseDouble(map_radius))
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(32,255,0,0)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(15)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentScheduleAddMap.this, parentScheduleAdd.class);
                intent.putExtra("map_latitude", map_latitude);
                intent.putExtra("map_longitude", map_longitude);
                intent.putExtra("map_radius", map_radius);
                intent.putExtra("login", child);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.clear();
        LatLng latlng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        mMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)
                .zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map_latitude = Double.toString(latLng.latitude);
                map_longitude = Double.toString(latLng.longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, parentScheduleAdd.class);
        intent.putExtra("login", child);
        startActivity(intent);
    }

}