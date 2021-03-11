package com.example.bezpiecznedziecko.parent.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.parentLogin;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;
import com.example.bezpiecznedziecko.parent.maps.parentMap;
import com.example.bezpiecznedziecko.parent.schedules.parentSchedulesList;
import com.example.bezpiecznedziecko.parent.schedules.parentSchedulesListView;
import com.example.bezpiecznedziecko.welcome;

public class parentMain extends AppCompatActivity {

    Button btn_maps, btn_children, btn_schedules, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_main);

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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}