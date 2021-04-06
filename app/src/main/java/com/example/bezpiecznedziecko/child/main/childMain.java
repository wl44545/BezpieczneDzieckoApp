package com.example.bezpiecznedziecko.child.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.childLogin;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.child.maps.childMap;
import com.example.bezpiecznedziecko.child.schedules.childSchedulesList;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.example.bezpiecznedziecko.parent.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.welcome;

public class childMain extends AppCompatActivity {

    Button btn_maps, btn_schedules, btn_logout;
    TextView txt_name, txt_login;
    Boolean is_child_logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_main);

        is_child_logged = getIntent().getBooleanExtra("is_child_logged", false);
        String login = getIntent().getStringExtra("login");
        String first_name = getIntent().getStringExtra("first_name");
        String last_name = getIntent().getStringExtra("last_name");
        String name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_name.setText(name);
        txt_login.setText(login);

        btn_maps = (Button)findViewById(R.id.btn_maps);
        btn_maps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(childMain.this, childMap.class);
                startActivity(intent);
            }
        });
        btn_schedules = (Button)findViewById(R.id.btn_schedules);
        btn_schedules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(childMain.this, childSchedulesList.class);
                startActivity(intent);
            }
        });
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(childMain.this, welcome.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}