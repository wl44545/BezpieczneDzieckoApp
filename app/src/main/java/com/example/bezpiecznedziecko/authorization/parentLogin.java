package com.example.bezpiecznedziecko.authorization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.child.main.childMain;
import com.example.bezpiecznedziecko.parent.main.parentMain;
import com.example.bezpiecznedziecko.parent.schedules.Schedules;
import com.example.bezpiecznedziecko.retrofit.RestClient;
import com.example.bezpiecznedziecko.welcome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bezpiecznedziecko.retrofit.RestClient.BASE_URL;

public class parentLogin extends AppCompatActivity {

    EditText edt_login, edt_password;
    Button btn_login, btn_back;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_login);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        edt_login = (EditText)findViewById(R.id.edt_login);
        edt_password = (EditText)findViewById(R.id.edt_password);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginParent(edt_login.getText().toString(),
                            edt_password.getText().toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
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

    private String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private void loginParent(String login, String plain_password) throws IOException, JSONException {
        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "Wprowadź login", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(plain_password)){
            Toast.makeText(this, "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "3rcc4zyKsMBESXQGtKbVrv1Za8l3CwB5ndIRdG25S3aarrhkCSGtO8SoGERIATen";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String x = "http://10.0.2.2:8080/parents?token="+token+"&login="+login;

        URL url = new URL(x);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String res_salt = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("salt");
        String res_password = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("password");
        String res_first_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("first_name");
        String res_last_name = (String) new JSONObject(content.toString().replace('[',' ').replace(']', ' ')).get("last_name");

        String password = get_SHA_512_SecurePassword(plain_password, res_salt);

        if(password.equals(res_password)){
            Toast.makeText(this, "Zalogowano", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(parentLogin.this, parentMain.class);
            intent.putExtra("login", login);
            intent.putExtra("first_name", res_first_name);
            intent.putExtra("last_name", res_last_name);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Złe hasło", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}