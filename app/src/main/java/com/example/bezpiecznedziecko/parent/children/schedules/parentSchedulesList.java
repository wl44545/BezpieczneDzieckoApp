package com.example.bezpiecznedziecko.parent.children.schedules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.authorization.childRegister;
import com.example.bezpiecznedziecko.parent.children.parentChildrenList;
import com.example.bezpiecznedziecko.parent.children.schedules.parentSchedulesListView.OnNoteListener;
import com.example.bezpiecznedziecko.retrofit.RestClient;
import com.example.bezpiecznedziecko.welcome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class parentSchedulesList extends AppCompatActivity implements OnNoteListener {

    RecyclerView recyclerView;
    Retrofit retrofit;
    parentSchedulesListView parentSchedulesListView;
    List<Schedules.Schedule> scheduleList;
    private OnNoteListener onNoteListener;
    String login;
    Button btn_add_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_schedules_list);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        btn_add_schedule = (Button)findViewById(R.id.btn_add_schedule);
        btn_add_schedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(parentSchedulesList.this, parentScheduleAdd.class);
                intent.putExtra("login", login);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentSchedulesListView = new parentSchedulesListView(this);
        recyclerView.setAdapter(parentSchedulesListView);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        callEndpoints();
    }

    @SuppressLint("CheckResult")
    private void callEndpoints() {
        login = getIntent().getStringExtra("login");
        RestClient retrofitService = retrofit.create(RestClient.class);
        Observable<Schedules> schedulesObservable = retrofitService.getParentChildrenSchedules(login);
        schedulesObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.data).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<Schedules.Schedule> schedules) {
        if (schedules != null && schedules.size() != 0) {
            parentSchedulesListView.setData(schedules);
            scheduleList = schedules;
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
        Intent intent = new Intent(this, parentSchedule.class);
        intent.putExtra("_id", scheduleList.get(position)._id);
        intent.putExtra("description",scheduleList.get(position).description);
        intent.putExtra("start",scheduleList.get(position).start);
        intent.putExtra("stop",scheduleList.get(position).stop);
        intent.putExtra("longitude",scheduleList.get(position).longitude);
        intent.putExtra("latitude",scheduleList.get(position).latitude);
        intent.putExtra("radius",scheduleList.get(position).radius);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}