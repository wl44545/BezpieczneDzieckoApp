package com.example.bezpiecznedziecko.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.example.bezpiecznedziecko.welcome;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class childRegister extends AppCompatActivity {

    EditText edt_login, edt_password, edt_first_name, edt_last_name, edt_email, edt_phone_number,
            edt_pesel, edt_adress, edt_postal_code, edt_city, edt_country;
    RadioButton radio_male, radio_female, radio_no;
    Button btn_register, btn_back;
    String txt_gender, txt_account_type, txt_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_register);

        txt_account_type = "Free";
        txt_parent = "tmp";


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

        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerChild(edt_login.getText().toString(), edt_password.getText().toString(), edt_first_name.getText().toString(),
                            edt_last_name.getText().toString(), edt_email.getText().toString(), edt_phone_number.getText().toString(),
                            edt_pesel.getText().toString(), txt_gender , txt_parent, txt_account_type);
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
                Intent intent = new Intent(childRegister.this, welcome.class);
                startActivity(intent);
            }
        });
    }

    private String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private String randomString(int len){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private void registerChild(String login, String plain_password, String first_name, String last_name,
                                String email, String phone_number, String pesel, String gender,
                                String txt_parent, String account_type) throws IOException, JSONException {

        String token = "GgV6r7hErAKK8ln7muz71FtM2sdI4yGaf2H6zpbrplBY6pvTjvqKAkW3cAbGyhhe";

        String salt = randomString(64);
        String password = get_SHA_512_SecurePassword(plain_password, salt);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/children");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //con.setRequestProperty("content-length", "512");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("token="+token+"&login="+login+"&password="+password+"&salt="+salt+"&first_name="+first_name+"&last_name="+last_name+"&email="+email+"&phone_number="+phone_number+"&pesel="+pesel+"&gender="+gender+"&parent="+txt_parent+"&account_type="+account_type);
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
        String response = (String) jsonObj.get("code");
        System.out.println(response);

        if(response.equals("-1")){
            Toast.makeText(this, "Błąd", Toast.LENGTH_SHORT).show();
        }
        else if(response.equals("0")){
            Toast.makeText(this, "Zarejestrowano", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(childRegister.this, parentMain.class);
            startActivity(intent);

        }
        else if(response.equals("1")){
            Toast.makeText(this, "Konto już istnieje", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}