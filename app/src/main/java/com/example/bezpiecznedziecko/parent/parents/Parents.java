package com.example.bezpiecznedziecko.parent.parents;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Parents {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Parent> data = null;

    public class Parent {
        @SerializedName("login")
        public String login;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
    }

}
