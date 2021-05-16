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
        @SerializedName("_id")
        public String _id;
        @SerializedName("child")
        public String child;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("radius")
        public String radius;
        @SerializedName("description")
        public String description;
        @SerializedName("start")
        public String start;
        @SerializedName("stop")
        public String stop;
    }

}
