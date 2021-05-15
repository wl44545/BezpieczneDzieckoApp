package com.example.bezpiecznedziecko.parent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.common.PassConfirmDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class parentDelete extends  AppCompatActivity implements PassConfirmDialog.PassConfirmDialogListener {
    private String login, salt, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_delete);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        try {
            getParentPassword(login);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        openDialog();
    }

    private void getParentPassword(String login) throws IOException, JSONException
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = "http://10.0.2.2:8080/parents?token="+getString(R.string.parent_token)+
                "&login="+login;//+"&parent=0";

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

        password = (String) new JSONObject(content.toString().replace('[',' ').
                replace(']', ' ')).get("password");
        salt = (String) new JSONObject(content.toString().replace('[',' ').
                replace(']', ' ')).get("salt");
    }

    public void openDialog()
    {
        PassConfirmDialog passConfirmDialog = new PassConfirmDialog(R.layout.parent_delete_confirm);
        passConfirmDialog.show(getSupportFragmentManager(), "Password Confirmation");
    }

    private void deleteParent(String login) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = getString(R.string.base_url)+"parents?token="+getString(R.string.parent_token)
                +"&login="+login;

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

        System.out.println(content);


        JSONObject jsonObj = new JSONObject(content.toString());
        String response = (String) jsonObj.get("code");
        System.out.println(response);

        if(response.equals("-1")){
            Toast.makeText(this, "Błąd", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }
        else if(response.equals("0")){
            Toast.makeText(this, "Usunięto konto.", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    }

    @Override
    public void confirmCorrectPassword(AlertDialog confirmDialog, TextInputLayout passwordInputLayout, String enteredPlainPassword){
        com.example.bezpiecznedziecko.authorization.safetyFunctions safetyFunctions = new com.example.bezpiecznedziecko.authorization.safetyFunctions();
        String hashedGivenPassword = safetyFunctions.get_SHA_512_SecurePassword(enteredPlainPassword,salt);
        System.out.println("hashedGivenPassword: "+hashedGivenPassword);
        System.out.println("currentPassword: "+password);

        if(!hashedGivenPassword.equals(password))
            passwordInputLayout.setError("Nieprawidłowe hasło!");
        else
        {
            try {
                deleteParent(login);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            confirmDialog.dismiss();
        }
    }

}
