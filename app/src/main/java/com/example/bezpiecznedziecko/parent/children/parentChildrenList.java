package com.example.bezpiecznedziecko.parent.children;

import android.annotation.SuppressLint;
import android.content.Intent;
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
        RestClient retrofitService = retrofit.create(RestClient.class);
        Observable<Children> childrenObservable = retrofitService.getChildrenProfiles();
        childrenObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.data).subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<Children.Child> children) {
        if (children != null && children.size() != 0) {
            parentChildrenListView.setData(children);
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
        Intent intent = new Intent(this, welcome.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}