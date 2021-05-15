package com.example.bezpiecznedziecko.retrofit;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.Children;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestClient {
    //String BASE_URL = getString(R.string.base_url);

    @GET("/children?token=GgV6r7hErAKK8ln7muz71FtM2sdI4yGaf2H6zpbrplBY6pvTjvqKAkW3cAbGyhhe&login=0")
    Observable<Children> getChildrenProfiles(@Query("parent") String parent);

    @GET("schedules?token=WBLGQ1u9bOVoVRM3XWLYmGg2iuUXTlGgnSmDjgdVvyJQPWkdjwC6CiykdTIwEPcr&type=parent")
    Observable<com.example.bezpiecznedziecko.parent.children.schedules.Schedules> getParentsSchedules(@Query("parent") String parent);

    @GET("/schedules?token=WBLGQ1u9bOVoVRM3XWLYmGg2iuUXTlGgnSmDjgdVvyJQPWkdjwC6CiykdTIwEPcr&type=child")
    Observable<com.example.bezpiecznedziecko.child.schedules.Schedules> getChildrenChildrenSchedules(@Query("child") String child);

    @GET("/schedules?token=WBLGQ1u9bOVoVRM3XWLYmGg2iuUXTlGgnSmDjgdVvyJQPWkdjwC6CiykdTIwEPcr&type=child")
    Observable<com.example.bezpiecznedziecko.parent.children.schedules.Schedules> getParentChildrenSchedules(@Query("child") String child);



}
