package com.example.bezpiecznedziecko.parent.children;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.parent.main.parentMain;

public class parentChildProfile extends AppCompatActivity {

    Button btn_edit, btn_delete, btn_schedules;
    TextView txt_name, txt_login;
    String login, first_name, last_name, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_child_profile);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");

        name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_name.setText(name);
        txt_login.setText(login);


        btn_schedules = (Button)findViewById(R.id.btn_schedules);
        btn_schedules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentChildProfile.this, parentSchedulesList.class);
                startActivity(intent);
            }
        });

    }
}