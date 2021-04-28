package com.example.bezpiecznedziecko.parent.children;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.childRegister;
import com.example.bezpiecznedziecko.parent.children.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.parent.main.parentMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class parentChildEdit extends AppCompatActivity {

    Button btn_save;
    RadioButton radio_male, radio_female, radio_no;
    String login, password, salt, first_name, last_name, email, phone_number, pesel, gender, txt_parent;
    EditText edt_login, edt_password, edt_first_name, edt_last_name, edt_email, edt_phone_number,
            edt_pesel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_child_edit);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        try {
            loadChild(login);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        edt_login = (EditText)findViewById(R.id.edt_login);
        edt_password = (EditText)findViewById(R.id.edt_password);
        edt_first_name = (EditText)findViewById(R.id.edt_first_name);
        edt_last_name = (EditText)findViewById(R.id.edt_last_name);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_phone_number = (EditText)findViewById(R.id.edt_phone_number);
        edt_pesel = (EditText)findViewById(R.id.edt_pesel);

        edt_login.setText(login);
        edt_password.setText(password);
        edt_first_name.setText(first_name);
        edt_last_name.setText(last_name);
        edt_email.setText(email);
        edt_phone_number.setText(phone_number);
        edt_pesel.setText(pesel);

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

            }
        });

    }

    private void loadChild(String login) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = "http://10.0.2.2:8080/children?token="+getString(R.string.child_token)+"&login="+login+"&parent=0";

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

        password = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("password");
        salt = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("salt");
        first_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("first_name");
        last_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("last_name");
        email = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("email");
        phone_number = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("phone_number");
        pesel = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("pesel");
        gender = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("gender");
        txt_parent = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("parent");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}