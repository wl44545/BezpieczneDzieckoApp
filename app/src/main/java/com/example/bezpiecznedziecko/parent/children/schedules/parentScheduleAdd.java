package com.example.bezpiecznedziecko.parent.children.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class parentScheduleAdd extends AppCompatActivity {

    Button btn_save;
    EditText edt_start,edt_stop,edt_latitude,edt_longitude,edt_radius,edt_description;
    String txt_child, txt_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule_add);

        Intent intent = getIntent();
        txt_child = intent.getStringExtra("login");
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        txt_parent = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        edt_start = (EditText)findViewById(R.id.edt_start);
        edt_stop = (EditText)findViewById(R.id.edt_stop);
        edt_latitude = (EditText)findViewById(R.id.edt_latitude);
        edt_longitude = (EditText)findViewById(R.id.edt_longitude);
        edt_radius = (EditText)findViewById(R.id.edt_radius);
        edt_description = (EditText)findViewById(R.id.edt_description);

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                try {
                    addSchedule(txt_child,txt_parent,edt_start.getText().toString(),edt_stop.getText().toString(),edt_latitude.getText().toString(),edt_longitude.getText().toString(),edt_radius.getText().toString(),edt_description.getText().toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void addSchedule(String child, String parent, String start, String stop, String latitude, String longitude,
                               String radius, String description) throws IOException, JSONException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/schedules");
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