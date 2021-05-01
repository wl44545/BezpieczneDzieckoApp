package com.example.bezpiecznedziecko.parent.children;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.safetyFunctions;
import com.example.bezpiecznedziecko.common.PassConfirmDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class parentChildEdit extends AppCompatActivity implements PassConfirmDialog.PassConfirmDialogListener {

    Button btn_save;
//    RadioButton radio_male, radio_female, radio_no;
    String login, password, salt, first_name, last_name, email, phone_number, pesel, gender, txt_parent, account_type;
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
//        edt_password.setText(password);
        edt_first_name.setText(first_name);
        edt_last_name.setText(last_name);
        edt_email.setText(email);
        edt_phone_number.setText(phone_number);
        if(!pesel.equals(" "))
        {
            edt_pesel.setText(pesel);
        }
        loadGender();


        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String newPassword = edt_password.getText().toString();

                if(newPassword.equals(""))
                {
                    System.out.println("nie wprowadzono nowego hasła");
                    prepareToUpdateChild();
                    //TODO: update data - send them to the database
                }
                else
                {
                    System.out.println("wprowadzono nowe hasło");
                    System.out.println("current password: "+password);
                    openDialog();
                }

                //TODO: probably I can do it here - in order to not multiply code (reduce one if's branch above)
//                try {
//                    deleteChild(login);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }
        });

    }

    private void prepareToUpdateChild() {
        String tmp;
        boolean passwordChanged = false;

        tmp = edt_login.getText().toString();
        if(!tmp.equals(""))
            login = tmp;
        tmp = edt_password.getText().toString();
        if(!tmp.equals(""))
        {
            password = tmp;
            passwordChanged=true;
        }
        tmp = edt_first_name.getText().toString();
        if(!tmp.equals(""))
            first_name = tmp;
        tmp = edt_last_name.getText().toString();
        if(!tmp.equals(""))
            last_name = tmp;
        tmp = edt_email.getText().toString();
        if(!tmp.equals(""))
            email = tmp;
        tmp = edt_phone_number.getText().toString();
        if(!tmp.equals(""))
            phone_number = tmp;
        tmp = edt_pesel.getText().toString();
        if(!tmp.equals(""))
            pesel = tmp;

        try {
            registerChild(passwordChanged,login,salt,password,first_name,last_name,email,phone_number,txt_parent,account_type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
//        account_type = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("account_type");

    }

    //account type is not given into consideration in rest api server
    private void registerChild(boolean passwordChanged, String login, String salt, String plain_password, String first_name, String last_name,
                               String email, String phone_number,
                               String txt_parent, String account_type) throws IOException, JSONException {

        String password = plain_password;
        if(passwordChanged)
        {
            safetyFunctions safetyFunctions = new safetyFunctions();
            password = safetyFunctions.get_SHA_512_SecurePassword(plain_password, salt);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/children");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //con.setRequestProperty("content-length", "512");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        String outline = "token="+getString(R.string.child_token)+"&login="+login+"&password="+password+"&salt="+salt+"&first_name="+first_name+"&last_name="+last_name+"&email="+email+"&phone_number="+phone_number+"&parent="+txt_parent+"&pesel="+pesel+"&gender="+gender;
        System.out.println(outline);
        out.writeBytes(outline);//"&pesel="+pesel+"&gender="+gender+"&parent="+txt_parent+"&account_type="+account_type);
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

        System.out.println(content);


        JSONObject jsonObj = new JSONObject(content.toString());
        String response = (String) jsonObj.get("code");
        System.out.println(response);

        if(response.equals("-1")){
            Toast.makeText(this, "Błąd", Toast.LENGTH_SHORT).show();
        }
        else if(response.equals("0")){
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("login",login);
//            returnIntent.putExtra("first_name",first_name);
//            returnIntent.putExtra("last_name",last_name);
//            setResult(RESULT_OK,returnIntent);

            Toast.makeText(this, "Zaaktualizowano dane.", Toast.LENGTH_SHORT).show();
            this.finish();
//            Intent intent = new Intent(childRegister.this, parentMain.class);
//            startActivity(intent);

        }
        else if(response.equals("1")){
            Toast.makeText(this, "Konto już istnieje", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteChild(String login) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = "http://10.0.2.2:8080/children?token="+getString(R.string.child_token)+"&login="+login;

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
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onGenderChoose(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked) {
                    Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
                    gender = "Male";
                }
                break;
            case R.id.radio_female:
                if (checked) {
                    Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
                    gender = "Female";
                }
                break;
            case R.id.radio_no:
                if (checked) {
                    Toast.makeText(this,"No gender",Toast.LENGTH_SHORT).show();
                    gender = "No gender";
                }
                break;
            default:
                gender = "";
                break;
        }
    }

    private void loadGender()
    {
        switch (gender) {
            case "Male":
                ((RadioButton) findViewById(R.id.radio_male)).setChecked(true);
                break;
            case "Female":
                ((RadioButton) findViewById(R.id.radio_female)).setChecked(true);
                break;
            case "No gender":
                ((RadioButton) findViewById(R.id.radio_no)).setChecked(true);
                break;
            default:
                break;
        }
    }

    public void openDialog()
    {
        PassConfirmDialog passConfirmDialog = new PassConfirmDialog();
        passConfirmDialog.show(getSupportFragmentManager(), "Password Confirmation");
    }

    @Override
    public void confirmCorrectPassword(AlertDialog confirmDialog, TextInputLayout passwordInputLayout, String enteredPlainPassword){
        com.example.bezpiecznedziecko.authorization.safetyFunctions safetyFunctions = new safetyFunctions();
        String hashedGivenPassword = safetyFunctions.get_SHA_512_SecurePassword(enteredPlainPassword,salt);
        System.out.println("hashedGivenPassword: "+hashedGivenPassword);
        System.out.println("currentPassword: "+password);

        if(!hashedGivenPassword.equals(password))
            passwordInputLayout.setError("Nieprawidłowe hasło!");
        else
        {
            confirmDialog.dismiss();
            prepareToUpdateChild();
        }

    }
}