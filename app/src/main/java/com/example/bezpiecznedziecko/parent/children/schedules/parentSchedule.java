package com.example.bezpiecznedziecko.parent.children.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bezpiecznedziecko.R;

public class parentSchedule extends AppCompatActivity {

    String _id;
    TextView txt_id;
    Button btn_edit, btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedule);

        Intent intent = getIntent();
        _id = intent.getStringExtra("_id");

        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_id.setText(_id);

    }
}