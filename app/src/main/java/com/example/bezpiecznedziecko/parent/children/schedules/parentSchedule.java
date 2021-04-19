package com.example.bezpiecznedziecko.parent.children.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.parentChildDelete;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;

import org.json.JSONException;

import java.io.IOException;

public class parentSchedule extends AppCompatActivity {

    String _id, login;
    TextView txt_id;
    Button btn_edit, btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule);

        Intent intent = getIntent();
        _id = intent.getStringExtra("_id");
        login = intent.getStringExtra("login");

        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_id.setText(_id);

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(parentSchedule.this, parentScheduleEdit.class);
                intent.putExtra("login",login);
                intent.putExtra("_id",_id);
                startActivity(intent);
            }
        });

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(parentSchedule.this, parentScheduleDelete.class);
                intent.putExtra("login",login);
                intent.putExtra("_id",_id);
                startActivity(intent);
            }
        });

    }
}