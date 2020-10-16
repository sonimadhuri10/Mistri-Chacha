package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FinalCategoryModel {

    // Product with category

    @SerializedName("result")
    public ArrayList<MainListModel> mainlist = new ArrayList<>();

    public static class MainListModel {

        @SerializedName("name")
        public String name;

        @SerializedName("path")
        public String path;

        @SerializedName("img")
        public String img;

        @SerializedName("image")
        public ArrayList<Imagelist> imageList = new ArrayList<Imagelist>();

        @SerializedName("products")
        public ArrayList<productlist> productlists = new ArrayList<>();

    }

    public static class productlist {

        @SerializedName("id")
        public String id;

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

        @SerializedName("type")
        public String type;

        @SerializedName("discount")
        public String discount;

        @SerializedName("discount_date")
        public String discount_date;


    }

    public static class Imagelist {

        @SerializedName("id")
        public String id;

        @SerializedName("photo")
        public String photo;

        @SerializedName("link")
        public String link;

    }


    // Partner

    @SerializedName("partners")
    public ArrayList<PartnerData> partner_array_list = new ArrayList<>();

    public static class PartnerData {

        @SerializedName("id")
        public String id;

        @SerializedName("photo")
        public String photo;

        @SerializedName("link")
        public String link;

    }

    // Categories


    @SerializedName("categories")
    public ArrayList<Category_Data_list> category_array_list = new ArrayList<>();

    public static class Category_Data_list {

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


    // Sliders

    @SerializedName("PhoneSliders")
    public ArrayList<Commanslider_Data_list> slider_list = new ArrayList<>();

    public static class Commanslider_Data_list {

        @SerializedName("id")
        public String id;

        @SerializedName("subtitle_text")
        public String subtitle_text;

        @SerializedName("photo")
        public String photo;

    }

}
