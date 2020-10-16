package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryModel {

    @SerializedName("categoriespath")
    public String categoriespath;

    @SerializedName("categories")
    public ArrayList<datalist> category_list = new ArrayList<>();

    public static class datalist {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("photo")
        public String photo;

        @SerializedName("image")
        public String image;

        @SerializedName("slug")
        public String slug;

    }
}
