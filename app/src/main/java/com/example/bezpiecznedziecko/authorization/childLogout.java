package com.example.bezpiecznedziecko.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.child.main.childMain;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.example.bezpiecznedziecko.welcome;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class childLogout extends AppCompatActivity {

    EditText edt_password;
    Button btn_logout;
    String parent_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_logout);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        parent_login = sharedPref.getString("parent",null);

        edt_password = (EditText)findViewById(R.id.edt_password);

        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginParent(parent_login,
                            edt_password.getText().toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
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

    private void loginParent(String login, String plain_password) throws IOException, JSONException {
        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "Wprowadź login", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(plain_password)){
            Toast.makeText(this, "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = getString(R.string.base_url)+"parents?token="+ getString(R.string.parent_token)+"&login="+login;

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

        String res_salt = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("salt");
        String res_password = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("password");
        String res_first_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("first_name");
        String res_last_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("last_name");

        String password = get_SHA_512_SecurePassword(plain_password, res_salt);

        if(password.equals(res_password)){
            Toast.makeText(this, "Wylogowano", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(childLogout.this, welcome.class);

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.shared_preferences_logged), getString(R.string.shared_preferences_logged_nobody));
            editor.putString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_logged_nobody));
            editor.putString(getString(R.string.shared_preferences_first_name), getString(R.string.shared_preferences_logged_nobody));
            editor.putString(getString(R.string.shared_preferences_last_name), getString(R.string.shared_preferences_logged_nobody));
            editor.apply();

            startActivity(intent);
        }else{
            Toast.makeText(this, "Złe hasło", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, childMain.class);
        startActivity(intent);
    }

}