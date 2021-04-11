package com.example.bezpiecznedziecko.parent.children.schedules;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Schedules {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Schedule> data = null;

    public class Schedule {
        @SerializedName("child")
        public String child;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("latitude")
        public String latitude;
    }

}
