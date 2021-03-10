package com.example.bezpiecznedziecko.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.schedules.Schedules;
import com.example.bezpiecznedziecko.welcome;

public class parentLogin extends AppCompatActivity {

    EditText edt_login, edt_password;
    Button btn_login, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_login);

        edt_login = (EditText)findViewById(R.id.edt_login);
        edt_password = (EditText)findViewById(R.id.edt_password);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginParent(edt_login.getText().toString(),
                        edt_password.getText().toString());
            }
        });
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentLogin.this, welcome.class);
                startActivity(intent);
            }
        });
    }
    private void loginParent(String login, String password){
        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "Wprowadź login", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "TU: FUNKCJA LOGOWANIA", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}