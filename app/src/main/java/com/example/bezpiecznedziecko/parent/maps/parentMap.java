package com.example.bezpiecznedziecko.parent.maps;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.Children;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.example.bezpiecznedziecko.retrofit.RestClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class parentMap extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    String login;
    Retrofit retrofit;
    List<Children.Child> childrenList;
    Random rnd = new Random();
    int[][] colors = new int[255][3];
    boolean first_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_map);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_login));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        for(int i=0;i<255;i++){
            colors[i][0] = rnd.nextInt(256);
            colors[i][1] = rnd.nextInt(256);
            colors[i][2] = rnd.nextInt(256);
        }

        first_map = true;
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
        mMap.addMarker(new MarkerOptions().icon(vectorToBitmap(R.drawable.ic_parent, Color.BLUE)).position(latlng).title("Tu jesteś"));

        callEndpoints();

        if(first_map){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlng)      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        first_map = false;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, parentMain.class);
        startActivity(intent);
    }


    @SuppressLint("CheckResult")
    private void callEndpoints() {

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        RestClient retrofitService = retrofit.create(RestClient.class);
        Observable<Children> childrenObservable = retrofitService.getChildrenProfiles(login);
        childrenObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.data).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<Children.Child> children) throws IOException, JSONException {
        if (children != null && children.size() != 0) {
            childrenList = children;
            System.out.println(childrenList);

            for(int i=0;i<children.size();i++){

                loadCurrentSchedule(childrenList.get(i).login, colors[i][0],colors[i][2],colors[i][2]);
                loadCurrentLocation(childrenList.get(i).login, colors[i][0],colors[i][2],colors[i][2]);
            }

        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    private void loadCurrentSchedule(String login,int r,int g,int b) throws IOException, JSONException {
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
        String schedule_longitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("longitude");
        String schedule_latitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("latitude");
        String schedule_radius = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("radius");
        int clr = Color.argb(100,r,g,b);
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(schedule_latitude), Double.parseDouble(schedule_longitude)))
                .radius(Double.parseDouble(schedule_radius))
                .strokeColor(clr)
                .fillColor(clr));
        mMap.addMarker(new MarkerOptions().icon(vectorToBitmap(R.drawable.ic_schedule, clr)).position(new LatLng(Double.parseDouble(schedule_latitude), Double.parseDouble(schedule_longitude))).title("Tu powinno być dziecko: "+login));
    }


    private void loadCurrentLocation(String login,int r, int g, int b) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String x = getString(R.string.base_url)+"locations?token="+getString(R.string.schedule_token)+"&child="+login;
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
        String schedule_longitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("longitude");
        String schedule_latitude = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("latitude");
        String schedule_location = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("location");
        int clr = Color.argb(255,r,g,b);
        mMap.addMarker(new MarkerOptions().icon(vectorToBitmap(R.drawable.ic_person, clr)).position(new LatLng(Double.parseDouble(schedule_latitude), Double.parseDouble(schedule_longitude))).title("Tu jest dziecko: "+login));



    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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