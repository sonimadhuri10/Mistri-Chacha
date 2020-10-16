package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewModel {

    @SerializedName("Rating")
    public ArrayList<reviewlist> r_list = new ArrayList<>();

    public static class reviewlist {

        @SerializedName("review")
        public String review;

        @SerializedName("review_date")
        public String review_date;

        @SerializedName("name")
        public String username;

        @SerializedName("photo")
        public String icon;


    }

    @SerializedName("Comment")
    public ArrayList<commentlist> c_list = new ArrayList<>();

    public static class commentlist {

        @SerializedName("text")
        public String text;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("name")
        public String username;

        @SerializedName("photo")
        public String icon;


    }
}
