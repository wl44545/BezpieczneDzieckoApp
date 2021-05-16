package com.example.bezpiecznedziecko.parent.children;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.schedules.parentSchedulesList;

public class parentChildProfile extends AppCompatActivity {

    Button btn_edit, btn_delete, btn_schedules;
    TextView txt_name, txt_login;
    String login, first_name, last_name, name;

//    public static final int EDIT_UPDATED_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_child_profile);
        System.out.println(this.getLocalClassName().toString()+": onCreate");

//        Intent intent = getIntent();
//        login = intent.getStringExtra("login");
//        first_name = intent.getStringExtra("first_name");
//        last_name = intent.getStringExtra("last_name");

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        login = sharedPreferences.getString(getString(R.string.shared_preferences_child_login),"");
        first_name = sharedPreferences.getString(getString(R.string.shared_preferences_child_first_name),"");
        last_name = sharedPreferences.getString(getString(R.string.shared_preferences_child_last_name),"");

        name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.view_txt2);
        txt_login = (TextView) findViewById(R.id.view_txt1);
        txt_name.setText(name);
        txt_login.setText(login);


        btn_schedules = (Button)findViewById(R.id.btn_schedules);
        btn_schedules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentChildProfile.this, parentSchedulesList.class);
                intent.putExtra("login",login);
                startActivity(intent);
            }
        });

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentChildProfile.this, parentChildDelete.class);
                intent.putExtra("login",login);
                intent.putExtra("first_name",first_name);
                intent.putExtra("last_name",last_name);
                startActivity(intent);
            }
        });

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentChildProfile.this, parentChildEdit.class);
//                intent.putExtra("login",login);
//                intent.putExtra("first_name",first_name);
//                intent.putExtra("last_name",last_name);
//                startActivityForResult(intent,EDIT_UPDATED_DATA);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(this.getLocalClassName().toString()+": onResume");

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        login = sharedPreferences.getString(getString(R.string.shared_preferences_child_login),"");
        first_name = sharedPreferences.getString(getString(R.string.shared_preferences_child_first_name),"");
        last_name = sharedPreferences.getString(getString(R.string.shared_preferences_child_last_name),"");

        name = first_name + " " + last_name;

        txt_name.setText(name);
        txt_login.setText(login);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RESULT_OK && resultCode == EDIT_UPDATED_DATA)
//        {
//            try {
//                login = data.getStringExtra("login");
//                first_name = data.getStringExtra("first_name");
//                last_name = data.getStringExtra("last_name");
//            } catch (NullPointerException e) {
//                throw new NullPointerException(e.toString()+" attempt to retrieve data from Intent which doesn't exist");
//            }
//
//        }
//    }
}