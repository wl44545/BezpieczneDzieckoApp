package com.example.bezpiecznedziecko.parent.children.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.parentChildDelete;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class parentScheduleDelete extends AppCompatActivity {

    Button btn_delete;
    String _id, login, child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule_delete);

        Intent intent = getIntent();
        _id = intent.getStringExtra("_id");
        login = intent.getStringExtra("login");
        child = intent.getStringExtra("child");

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                try {
                    deleteSchedule(_id);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(parentScheduleDelete.this, parentSchedulesList.class);
                intent.putExtra("login", login);
                startActivity(intent);
            }
        });
    }

    private void deleteSchedule(String _id) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = getString(R.string.base_url)+"schedules?token="+getString(R.string.schedule_token)+"&_id="+_id;

        URL url = new URL(x);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, parentSchedule.class);
        intent.putExtra("_id",_id);
        intent.putExtra("login",login);
        intent.putExtra("child",child);
        startActivity(intent);
    }

}