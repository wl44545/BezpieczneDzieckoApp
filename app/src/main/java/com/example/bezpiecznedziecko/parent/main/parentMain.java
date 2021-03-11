package com.example.bezpiecznedziecko.parent.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.childRegister;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;
import com.example.bezpiecznedziecko.parent.maps.parentMap;
import com.example.bezpiecznedziecko.parent.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.parent.schedules.parentSchedulesListView;
import com.example.bezpiecznedziecko.welcome;

public class parentMain extends AppCompatActivity {

    Button btn_maps, btn_children, btn_schedules, btn_logout, btn_add_child;
    TextView txt_name, txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_main);

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
                Intent intent = new Intent(parentMain.this, parentMap.class);
                startActivity(intent);
            }
        });
        btn_children = (Button)findViewById(R.id.btn_children);
        btn_children.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentMain.this, parentChildrenList.class);
                startActivity(intent);
            }
        });
        btn_schedules = (Button)findViewById(R.id.btn_schedules);
        btn_schedules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentMain.this, parentSchedulesList.class);
                startActivity(intent);
            }
        });
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentMain.this, welcome.class);
                startActivity(intent);
            }
        });

        btn_add_child = (Button)findViewById(R.id.btn_add_child);
        btn_add_child.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentMain.this, childRegister.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}