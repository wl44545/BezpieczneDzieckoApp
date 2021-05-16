package com.example.bezpiecznedziecko.parent.children.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.childRegister;
import com.example.bezpiecznedziecko.parent.main.parentMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class parentScheduleAdd extends AppCompatActivity {

    Button btn_save, btn_map, btn_start_date, btn_start_time, btn_stop_date, btn_stop_time;
    EditText edt_description;
    TextView txt_start_date, txt_stop_date, txt_start_time, txt_stop_time, txt_longitude, txt_latitude, txt_radius;
    String map_latitude, map_longitude, map_radius, in_start_date, in_start_time, in_stop_date, in_stop_time, start, stop, child, parent;

    private static final int start_date_id = 0;
    private static final int start_time_id = 1;
    private static final int stop_date_id = 2;
    private static final int stop_time_id = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule_add);

        Intent intent = getIntent();
        child = intent.getStringExtra("login");
        map_latitude = intent.getStringExtra("map_latitude");
        map_longitude = intent.getStringExtra("map_longitude");
        map_radius = intent.getStringExtra("map_radius");

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        parent = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        edt_description = (EditText)findViewById(R.id.edt_description);
        txt_start_date = (TextView)findViewById(R.id.txt_start_date);
        txt_stop_date = (TextView)findViewById(R.id.txt_stop_date);
        txt_start_time = (TextView)findViewById(R.id.txt_start_time);
        txt_stop_time = (TextView)findViewById(R.id.txt_stop_time);
        txt_longitude = (TextView)findViewById(R.id.txt_longitude);
        txt_latitude = (TextView)findViewById(R.id.txt_latitude);
        txt_radius = (TextView)findViewById(R.id.txt_radius);

        txt_latitude.setText(map_latitude);
        txt_longitude.setText(map_longitude);
        txt_radius.setText(map_radius);
        /*txt_start_date.setText(in_start_date);
        txt_start_time.setText(in_start_time);
        txt_stop_date.setText(in_stop_date);
        txt_stop_time.setText(in_stop_time);*/

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                start = in_start_date + "T" + in_start_time;
                stop = in_stop_date + "T" + in_stop_time;
                try {
                    addSchedule(child,parent,start,stop,map_latitude,map_longitude,map_radius,edt_description.getText().toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_map = (Button)findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentScheduleAdd.this, parentScheduleAddMap.class);
                startActivity(intent);
            }
        });

        btn_start_date = (Button)findViewById(R.id.btn_start_date);
        btn_start_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showDialog(start_date_id);
            }
        });
        btn_start_time = (Button)findViewById(R.id.btn_start_time);
        btn_start_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showDialog(start_time_id);
            }
        });
        btn_stop_date = (Button)findViewById(R.id.btn_stop_date);
        btn_stop_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showDialog(stop_date_id);
            }
        });
        btn_stop_time = (Button)findViewById(R.id.btn_stop_time);
        btn_stop_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showDialog(stop_time_id);
            }
        });

    }

    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (id) {
            case start_date_id:
                return new DatePickerDialog(parentScheduleAdd.this, start_date_listener, year,
                        month, day);
            case start_time_id:
                return new TimePickerDialog(parentScheduleAdd.this, start_time_listener, hour,
                        minute, false);
            case stop_date_id:
                return new DatePickerDialog(parentScheduleAdd.this, stop_date_listener, year,
                        month, day);
            case stop_time_id:
                return new TimePickerDialog(parentScheduleAdd.this, stop_time_listener, hour,
                        minute, false);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener start_date_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            in_start_date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            txt_start_date.setText(in_start_date);
        }
    };
    TimePickerDialog.OnTimeSetListener start_time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            in_start_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":00.000+00:00";
            txt_start_time.setText(in_start_time);
        }
    };
    DatePickerDialog.OnDateSetListener stop_date_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            in_stop_date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            txt_stop_date.setText(in_stop_date);
        }
    };
    TimePickerDialog.OnTimeSetListener stop_time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            in_stop_time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":00.000+00:00";
            txt_stop_time.setText(in_stop_time);
        }
    };

    private void addSchedule(String child, String parent, String start, String stop, String latitude, String longitude,
                               String radius, String description) throws IOException, JSONException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL(getString(R.string.base_url)+"schedules");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("token="+getString(R.string.schedule_token)+"&child="+child+"&parent="+parent+"&start="+start+"&stop="+stop+"&latitude="+latitude+"&longitude="+longitude+"&radius="+radius+"&description="+description);
        out.flush();
        out.close();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        /*JSONObject jsonObj = new JSONObject(content.toString());
        String response = (String) jsonObj.get("code");
        System.out.println(response);*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}