package com.example.bezpiecznedziecko.child.schedules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.child.schedules.childSchedulesListView.OnNoteListener;
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
import com.example.bezpiecznedziecko.child.schedules.Schedules;


public class childSchedulesList extends AppCompatActivity implements OnNoteListener {

    RecyclerView recyclerView;
    Retrofit retrofit;
    childSchedulesListView childSchedulesListView;
    List<Schedules.Schedule> scheduleList;
    private OnNoteListener onNoteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_schedules_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childSchedulesListView = new childSchedulesListView(this);
        recyclerView.setAdapter(childSchedulesListView);

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
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String login = sharedPref.getString(getString(R.string.shared_preferences_login), "login");

        RestClient retrofitService = retrofit.create(RestClient.class);
        Observable<Schedules> schedulesObservable = retrofitService.getChildrenChildrenSchedules(login);
        schedulesObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.data).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<Schedules.Schedule> schedules) {
        if (schedules != null && schedules.size() != 0) {
            childSchedulesListView.setData(schedules);
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
        Intent intent = new Intent(this, childSchedule.class);
        intent.putExtra("_id", scheduleList.get(position)._id);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}