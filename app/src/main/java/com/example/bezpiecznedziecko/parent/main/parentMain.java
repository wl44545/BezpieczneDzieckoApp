package com.example.bezpiecznedziecko.parent.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;
import com.example.bezpiecznedziecko.parent.maps.parentMap;
import com.example.bezpiecznedziecko.welcome;

public class parentMain extends AppCompatActivity{

    Button btn_maps, btn_edit, btn_children, btn_delete, btn_logout;
    TextView txt_name, txt_login;
    String login,first_name,last_name,name;

    static final int DELETE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_main);
        setVisible(false);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");
        first_name = sharedPref.getString(getString(R.string.shared_preferences_first_name), "first_name");
        last_name = sharedPref.getString(getString(R.string.shared_preferences_last_name), "last_name");

        name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.view_txt2);
        txt_login = (TextView) findViewById(R.id.view_txt1);
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
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentMain.this, parentEdit.class);
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
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //TODO: create new transparent activity and on this show dialog
                startActivityForResult(new Intent(parentMain.this, parentDelete.class), DELETE_RESULT);
            }
        });
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.shared_preferences_logged), getString(R.string.shared_preferences_logged_nobody));
                editor.putString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_logged_nobody));
                editor.putString(getString(R.string.shared_preferences_first_name), getString(R.string.shared_preferences_logged_nobody));
                editor.putString(getString(R.string.shared_preferences_last_name), getString(R.string.shared_preferences_logged_nobody));
                editor.apply();
                Intent intent = new Intent(parentMain.this, welcome.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");
        first_name = sharedPref.getString(getString(R.string.shared_preferences_first_name), "first_name");
        last_name = sharedPref.getString(getString(R.string.shared_preferences_last_name), "last_name");

        name = first_name + " " + last_name;

        txt_name.setText(name);
        txt_login.setText(login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK  && requestCode == DELETE_RESULT)
            btn_logout.callOnClick();
    }
}