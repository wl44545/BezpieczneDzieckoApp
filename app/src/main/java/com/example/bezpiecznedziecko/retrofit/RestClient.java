package com.example.bezpiecznedziecko.retrofit;

import com.example.bezpiecznedziecko.parent.children.Children;
import com.example.bezpiecznedziecko.parent.parents.Parents;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestClient {
    String BASE_URL = "http://10.0.2.2:8080/";
    //String BASE_URL = "http://10.0.2.2:8080/children?token=GgV6r7hErAKK8ln7muz71FtM2sdI4yGaf2H6zpbrplBY6pvTjvqKAkW3cAbGyhhe&login=0/";

    @GET("/children?token=GgV6r7hErAKK8ln7muz71FtM2sdI4yGaf2H6zpbrplBY6pvTjvqKAkW3cAbGyhhe&login=0")
    Observable<Children> getChildrenProfiles();

    @GET("parents")
    Observable<Parents> getParentsProfiles();

    @GET("schedules")
    Observable<com.example.bezpiecznedziecko.parent.schedules.Schedules> getParentsSchedules();

    @GET("schedules")
    Observable<com.example.bezpiecznedziecko.child.schedules.Schedules> getChildrenSchedules();

    @FormUrlEncoded
    @POST("parent/login")
    Observable<String> loginParent(@Field("login") String login,
                                       @Field("password") String password);
}
