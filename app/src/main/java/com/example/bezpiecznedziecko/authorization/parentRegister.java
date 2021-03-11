package com.example.bezpiecznedziecko.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.welcome;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class parentRegister extends AppCompatActivity {

    EditText edt_login, edt_password, edt_first_name, edt_last_name, edt_email, edt_phone_number,
            edt_pesel, edt_adress, edt_postal_code, edt_city, edt_country;
    RadioButton radio_male, radio_female, radio_no;
    Button btn_register, btn_back;
    String txt_gender, txt_account_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_register);

        txt_account_type = "Free";

        radio_male = (RadioButton)findViewById(R.id.radio_male);
        radio_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_female.setChecked(false);
                radio_no.setChecked(false);
                txt_gender = "Mezczyzna";
            }
        });
        radio_female = (RadioButton)findViewById(R.id.radio_female);
        radio_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_male.setChecked(false);
                radio_no.setChecked(false);
                txt_gender = "Kobieta";
            }
        });
        radio_no = (RadioButton)findViewById(R.id.radio_no);
        radio_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_male.setChecked(false);
                radio_female.setChecked(false);
                txt_gender = "NiePodano";
            }
        });

        edt_login = (EditText)findViewById(R.id.edt_login);
        edt_password = (EditText)findViewById(R.id.edt_password);
        edt_first_name = (EditText)findViewById(R.id.edt_first_name);
        edt_last_name = (EditText)findViewById(R.id.edt_last_name);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_phone_number = (EditText)findViewById(R.id.edt_phone_number);
        edt_pesel = (EditText)findViewById(R.id.edt_pesel);
        edt_adress = (EditText)findViewById(R.id.edt_adress);
        edt_postal_code = (EditText)findViewById(R.id.edt_postal_code);
        edt_city = (EditText)findViewById(R.id.edt_city);
        edt_country = (EditText)findViewById(R.id.edt_country);

        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerParent(edt_login.getText().toString(), edt_password.getText().toString(), edt_first_name.getText().toString(),
                            edt_last_name.getText().toString(), edt_email.getText().toString(), edt_phone_number.getText().toString(),
                            edt_pesel.getText().toString(), txt_gender, edt_adress.getText().toString(), edt_postal_code.getText().toString(),
                            edt_city.getText().toString(), edt_country.getText().toString(), txt_account_type);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentRegister.this, welcome.class);
                startActivity(intent);
            }
        });
    }
    private void registerParent(String login, String password, String first_name, String last_name,
                                String email, String phone_number, String pesel, String gender,
                                String adress, String postal_code, String city, String country, String account_type) throws IOException, JSONException {

        String salt = "salt";
        String token = "3rcc4zyKsMBESXQGtKbVrv1Za8l3CwB5ndIRdG25S3aarrhkCSGtO8SoGERIATen";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/parents");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //con.setRequestProperty("content-length", "512");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("token="+token+"&login="+login+"&password="+password+"&salt="+salt+"&first_name="+first_name+"&last_name="+last_name+"&email="+email+"&phone_number="+phone_number+"&pesel="+pesel+"&gender="+gender+"&address="+adress+"&postal_code="+postal_code+"&city="+city+"&country="+country+"&account_type="+account_type);
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


        JSONObject jsonObj = new JSONObject(content.toString());
        System.out.println("Response status: " + status);
        System.out.println(jsonObj.get("status"));




    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}