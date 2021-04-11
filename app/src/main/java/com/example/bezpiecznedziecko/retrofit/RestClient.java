package com.example.bezpiecznedziecko.retrofit;

import com.example.bezpiecznedziecko.parent.children.Children;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestClient {
    String BASE_URL = "http://10.0.2.2:8080/";

    @GET("/children?token=GgV6r7hErAKK8ln7muz71FtM2sdI4yGaf2H6zpbrplBY6pvTjvqKAkW3cAbGyhhe&login=0")
    Observable<Children> getChildrenProfiles(@Query("parent") String parent);

    @GET("schedules")
    Observable<com.example.bezpiecznedziecko.parent.children.schedules.Schedules> getParentsSchedules();

    @GET("schedules")
    Observable<com.example.bezpiecznedziecko.child.schedules.Schedules> getChildrenSchedules();

}
