package com.example.bezpiecznedziecko.parent.children;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.parent.children.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.welcome;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class parentChildDelete extends AppCompatActivity {

    Button btn_delete;
    TextView txt_name, txt_login;
    String login, first_name, last_name, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_child_delete);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_name.setText(name);
        txt_login.setText(login);

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                try {
                    deleteChild(login);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(parentChildDelete.this, parentChildrenList.class);
                startActivity(intent);
            }
        });


    }

    private void deleteChild(String login) throws IOException, JSONException {

    }

}