package com.example.bezpiecznedziecko.parent.children;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Children {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Child> data = null;

    public class Child {
        @SerializedName("login")
        public String login;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
    }

}
