package com.example.bezpiecznedziecko.child.maps;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class childMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String schedule_longitude, schedule_latitude, schedule_radius, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_map);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_login));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        schedule_longitude = "0.0";
        schedule_latitude = "0.0";
        schedule_radius = "0.0";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        refresh(15000);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        float longitude = sharedPref.getFloat(getString(R.string.shared_preferences_longitude), (float) 0.0);
        float latitude = sharedPref.getFloat(getString(R.string.shared_preferences_latitude), (float) 0.0);
        LatLng latlng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Ja"));

        try {
            loadCurrentSchedule(login);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(schedule_latitude), Double.parseDouble(schedule_longitude)))
                .radius(Double.parseDouble(schedule_radius))
                .strokeColor(Color.RED)
                .fillColor(Color.argb(32,255,0,0)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void loadCurrentSchedule(String login) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String x = getString(R.string.base_url)+"schedules?token="+getString(R.string.schedule_token)+"&type=current&child="+login;
        URL url = new URL(x);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        schedule_longitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("longitude");
        schedule_latitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("latitude");
        schedule_radius = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("radius");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, childMain.class);
        startActivity(intent);
    }

    private void refresh (int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onMapReady(mMap);
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

}