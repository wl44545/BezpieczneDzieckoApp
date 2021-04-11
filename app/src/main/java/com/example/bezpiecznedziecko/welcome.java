package com.example.bezpiecznedziecko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.authorization.childLogin;
import com.example.bezpiecznedziecko.authorization.parentRegister;
import com.example.bezpiecznedziecko.child.main.childMain;
import com.example.bezpiecznedziecko.parent.main.parentMain;

public class welcome extends AppCompatActivity {

    Button btn_parent, btn_child, btn_register;
    String logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        logged = sharedPref.getString(getString(R.string.shared_preferences_logged), getString(R.string.shared_preferences_logged_nobody));

        if(logged.equals(getString(R.string.shared_preferences_logged_child))){
            Intent intent = new Intent(welcome.this, childMain.class);
            startActivity(intent);
        }
        else if(logged.equals(getString(R.string.shared_preferences_logged_parent))){
            Intent intent = new Intent(welcome.this, parentMain.class);
            startActivity(intent);
        }

        btn_parent = (Button)findViewById(R.id.btn_parent);
        btn_parent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(welcome.this, parentLogin.class);
                startActivity(intent);
            }
        });
        btn_child = (Button)findViewById(R.id.btn_child);
        btn_child.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(welcome.this, childLogin.class);
                startActivity(intent);
            }
        });
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(welcome.this, parentRegister.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}