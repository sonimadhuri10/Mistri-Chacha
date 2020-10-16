package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoryModel {

    @SerializedName("prods")
    public ArrayList<datalist> productlist = new ArrayList<>();

    public static class datalist {

        @SerializedName("id")
        public String id;

        @SerializedName("product_type")
        public String product_type;

        @SerializedName("category_id")
        public String category_id;

        @SerializedName("subcategory_id")
        public String subcategory_id;

        @SerializedName("childcategory_id")
        public String childcategory_id;

        @SerializedName("name")
        public String name;

        @SerializedName("thumbnail")
        public String thumbnail;

        @SerializedName("price")
        public String price;

        @SerializedName("previous_price")
        public String previous_price;

        @SerializedName("stock")
        public String stock;

        @SerializedName("slug")
        public String slug;

        @SerializedName("photo")
        public String photo;

        @SerializedName("discount")
        public String discount;

        @SerializedName("rating")
        public String rating;
    }



}
