package com.example.bezpiecznedziecko.retrofit;

import com.example.bezpiecznedziecko.parent.children.Children;
import com.example.bezpiecznedziecko.parent.parents.Parents;
import com.example.bezpiecznedziecko.parent.schedules.Schedules;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RestClient {
    String BASE_URL = "http://10.0.2.2:8080/";

    @GET("children")
    Observable<Children> getChildrenProfiles();

    @GET("parents")
    Observable<Parents> getParentsProfiles();

    @GET("schedules")
    Observable<Schedules> getSchedules();

}
