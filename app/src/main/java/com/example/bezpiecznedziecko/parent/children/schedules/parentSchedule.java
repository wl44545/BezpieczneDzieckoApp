package com.example.bezpiecznedziecko.parent.children.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.parentChildDelete;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;

import org.json.JSONException;

import java.io.IOException;

public class parentSchedule extends AppCompatActivity {

    String login, _id, description, start, stop, longitude, latitude, radius, child;
    TextView txt_description, txt_start, txt_stop, txt_longitude, txt_latitude, txt_radius;
    Button btn_edit, btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule);

        Intent intent = getIntent();
        child = intent.getStringExtra("child");
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

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentSchedule.this, parentScheduleEdit.class);
                intent.putExtra("login",login);
                intent.putExtra("_id",_id);
                startActivity(intent);
            }
        });

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentSchedule.this, parentScheduleDelete.class);
                intent.putExtra("login",login);
                intent.putExtra("_id",_id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, parentSchedulesList.class);
        intent.putExtra("login", child);
        startActivity(intent);
    }

}