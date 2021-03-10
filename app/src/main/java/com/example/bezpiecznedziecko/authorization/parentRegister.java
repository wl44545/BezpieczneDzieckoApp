package com.example.bezpiecznedziecko.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
                txt_gender = "Mężczyzna";
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
                registerParent(edt_login.getText().toString(), edt_password.getText().toString(), edt_first_name.getText().toString(),
                        edt_last_name.getText().toString(), edt_email.getText().toString(), edt_phone_number.getText().toString(),
                        edt_pesel.getText().toString(), txt_gender, edt_adress.getText().toString(), edt_postal_code.getText().toString(),
                        edt_city.getText().toString(), edt_country.getText().toString(), txt_account_type);
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
                                String adress, String postal_code, String city, String country, String account_type){
        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "Wprowadź login", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(first_name)){
            Toast.makeText(this, "Wprowadź imie", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(last_name)){
            Toast.makeText(this, "Wprowadź nazwisko", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Wprowadź email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone_number)){
            Toast.makeText(this, "Wprowadź numer telefonu", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pesel)){
            Toast.makeText(this, "Wprowadź numer pesel", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Wprowadź płeć", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(adress)){
            Toast.makeText(this, "Wprowadź adres", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(postal_code)){
            Toast.makeText(this, "Wprowadź kod pocztowy", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(city)){
            Toast.makeText(this, "Wprowadź miejscowość", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Wprowadź kraj", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "TU: FUNKCJA REJESTRACJI", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}