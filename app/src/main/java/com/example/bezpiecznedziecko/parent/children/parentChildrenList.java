package com.example.bezpiecznedziecko.parent.children;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.bezpiecznedziecko.welcome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.retrofit.RestClient;
import com.example.bezpiecznedziecko.parent.children.parentChildrenListView.OnNoteListener;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.bezpiecznedziecko.retrofit.RestClient.BASE_URL;

public class parentChildrenList extends AppCompatActivity implements OnNoteListener {

    RecyclerView recyclerView;
    Retrofit retrofit;
    parentChildrenListView parentChildrenListView;
    List<Children.Child> childrenList;

    private OnNoteListener onNoteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_children_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentChildrenListView = new parentChildrenListView (this);
        recyclerView.setAdapter(parentChildrenListView);

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
        callEndpoints();
    }

    @SuppressLint("CheckResult")
    private void callEndpoints() {

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        RestClient retrofitService = retrofit.create(RestClient.class);
        Observable<Children> childrenObservable = retrofitService.getChildrenProfiles(login);
        childrenObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.data).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<Children.Child> children) {
        if (children != null && children.size() != 0) {
            parentChildrenListView.setData(children);
            childrenList = children;
            System.out.println(children);

        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoteClick(int position) {
        System.out.println(childrenList);
        Intent intent = new Intent(this, parentChildProfile.class);
        intent.putExtra("login", childrenList.get(position).login);
        intent.putExtra("first_name", childrenList.get(position).first_name);
        intent.putExtra("last_name", childrenList.get(position).last_name);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}