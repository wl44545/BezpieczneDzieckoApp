package com.example.bezpiecznedziecko.parent.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class parentEdit extends AppCompatActivity implements PassConfirmDialog.PassConfirmDialogListener {

    Button btn_save;
    String login, password, salt, first_name, last_name, email, phone_number, pesel, gender,
            account_type, address, postal_code, city, country;
    EditText edt_login, edt_password, edt_first_name, edt_last_name, edt_email, edt_phone_number,
            edt_pesel, edt_account_type, edt_address, edt_postal_code, edt_city, edt_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_edit);

//        Intent intent = getIntent();
//        login = intent.getStringExtra("login");
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        login = sharedPreferences.getString(getString(R.string.shared_preferences_login),"");
        System.out.println("login: "+login);

        try {
            loadParent(login);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        edt_login = (EditText)findViewById(R.id.parent_edit_login);
        edt_password = (EditText)findViewById(R.id.parent_edit_password);
        edt_first_name = (EditText)findViewById(R.id.parent_edit_first_name);
        edt_last_name = (EditText)findViewById(R.id.parent_edit_last_name);
        edt_email = (EditText)findViewById(R.id.parent_edit_email);
        edt_phone_number = (EditText)findViewById(R.id.parent_edit_phone_number);
        edt_pesel = (EditText)findViewById(R.id.parent_edit_pesel);
        edt_account_type = (EditText)findViewById(R.id.parent_edit_account_type);
        edt_address = (EditText)findViewById(R.id.parent_edit_address);
        edt_postal_code = (EditText)findViewById(R.id.parent_edit_postal_code);
        edt_city = (EditText)findViewById(R.id.parent_edit_city);
        edt_country = (EditText)findViewById(R.id.parent_edit_country);

        edt_login.setText(login);
//        edt_password.setText(password);
        edt_first_name.setText(first_name);
        edt_last_name.setText(last_name);
        edt_email.setText(email);
        edt_phone_number.setText(phone_number);
        if(!pesel.equals(" "))
            edt_pesel.setText(pesel);
        edt_account_type.setText(account_type);
        if(!address.equals(" "))
            edt_address.setText(address);
        if(!postal_code.equals(" "))
            edt_postal_code.setText(postal_code);
        if(!city.equals(" "))
            edt_city.setText(city);
        if(!country.equals(" "))
            edt_country.setText(country);
        loadGender();

        //--------------------- to check -----------------
        btn_save = (Button)findViewById(R.id.parent_btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String newPassword = edt_password.getText().toString();

                if(newPassword.equals(""))
                    prepareToUpdateParent();
                else
                    openDialog();
            }
        });

    }

    private void prepareToUpdateParent() {
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
//        tmp = edt_account_type.getText().toString();
        tmp = edt_address.getText().toString();
        if(!tmp.equals(""))
            address = tmp;
        tmp = edt_postal_code.getText().toString();
        if(!tmp.equals(""))
            postal_code = tmp;
        tmp = edt_city.getText().toString();
        if(!tmp.equals(""))
            city = tmp;
        tmp = edt_country.getText().toString();
        if(!tmp.equals(""))
            country = tmp;

        try {
            updateParent(passwordChanged);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadParent(String login) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = "http://10.0.2.2:8080/parents?token="+getString(R.string.parent_token)+"&login="+login;//+"&parent=0";

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

//        String login, password, salt, first_name, last_name, email, phone_number, pesel, gender, account_type, address, postal_code, city, country;
        password = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("password");
        salt = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("salt");
        first_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("first_name");
        last_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("last_name");
        email = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("email");
        phone_number = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("phone_number");
        pesel = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("pesel");
        gender = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("gender");
//        txt_parent = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("parent");
        account_type = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("account_type");
        address = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("address");
        postal_code = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("postal_code");
        city = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("city");
        country = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("country");
    }

    //account type is not given into consideration in rest api server
    private void updateParent(boolean passwordChanged) throws IOException, JSONException {

        String password = this.password;
        if(passwordChanged)
        {
            safetyFunctions safetyFunctions = new safetyFunctions();
            password = safetyFunctions.get_SHA_512_SecurePassword(this.password, salt);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/parents");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //con.setRequestProperty("content-length", "512");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("token="+getString(R.string.parent_token)+"&login="+login+
                "&password="+password+"&salt="+salt+"&first_name="+first_name+
                "&last_name="+last_name+"&email="+email+"&phone_number="+phone_number+
                "&pesel="+pesel+"&gender="+gender+"&address="+address+"&postal_code="+postal_code+
                "&city="+city+"&country="+country+"&account_type="+account_type);
        //"&pesel="+pesel+"&gender="+gender+"&parent="+txt_parent+"&account_type="+account_type);
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
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.shared_preferences_login),login);
            editor.putString(getString(R.string.shared_preferences_first_name),first_name);
            editor.putString(getString(R.string.shared_preferences_last_name),last_name);
            editor.apply();

            Toast.makeText(this, "Zaaktualizowano dane.", Toast.LENGTH_SHORT).show();
            this.finish();
//            Intent intent = new Intent(childRegister.this, parentMain.class);
//            startActivity(intent);

        }
        else if(response.equals("1")){
            Toast.makeText(this, "Konto już istnieje", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onGenderChoose(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.parent_radio_male:
                if (checked) {
                    Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
                    gender = "Male";
                }
                break;
            case R.id.parent_radio_female:
                if (checked) {
                    Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
                    gender = "Female";
                }
                break;
            case R.id.parent_radio_no:
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
                ((RadioButton) findViewById(R.id.parent_radio_male)).setChecked(true);
                break;
            case "Female":
                ((RadioButton) findViewById(R.id.parent_radio_female)).setChecked(true);
                break;
            case "No gender":
                ((RadioButton) findViewById(R.id.parent_radio_no)).setChecked(true);
                break;
            default:
                break;
        }
    }

    public void openDialog()
    {
        PassConfirmDialog passConfirmDialog = new PassConfirmDialog(R.layout.parent_child_edit_confirm);
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
            prepareToUpdateParent();
        }

    }
}
