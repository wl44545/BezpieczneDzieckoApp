package com.example.bezpiecznedziecko.child.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bezpiecznedziecko.R;

public class childSchedule extends AppCompatActivity {

    String login, _id, description, start, stop, longitude, latitude, radius;
    TextView txt_description, txt_start, txt_stop, txt_longitude, txt_latitude, txt_radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_schedule);

        Intent intent = getIntent();
        _id = intent.getStringExtra("_id");
        login = intent.getStringExtra("login");
        description = intent.getStringExtra("description");
        start = intent.getStringExtra("start");
        stop = intent.getStringExtra("stop");
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");
        radius = intent.getStringExtra("radius");

        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_description.setText(description);
        txt_start = (TextView) findViewById(R.id.txt_start);
        txt_start.setText(start);
        txt_stop = (TextView) findViewById(R.id.txt_stop);
        txt_stop.setText(stop);
        txt_longitude = (TextView) findViewById(R.id.txt_longitude);
        txt_longitude.setText(longitude);
        txt_latitude = (TextView) findViewById(R.id.txt_latitude);
        txt_latitude.setText(latitude);
        txt_radius = (TextView) findViewById(R.id.txt_radius);
        txt_radius.setText(radius);

    }
}