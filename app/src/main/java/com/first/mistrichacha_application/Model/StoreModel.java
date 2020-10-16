package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreModel {

    @SerializedName("store")
    public ArrayList<store_list> storelist = new ArrayList<>();

    public static class store_list {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("city")
        public String city;

        @SerializedName("country")
        public String country;

        @SerializedName("shop_name")
        public String shop_name;

        @SerializedName("owner_name")
        public String owner_name;

        @SerializedName("shop_number")
        public String shop_number;

        @SerializedName("shop_address")
        public String shop_address;

        @SerializedName("email")
        public String email;

        @SerializedName("reg_number")
        public String reg_number;

        @SerializedName("photo")
        public String photo;

    }

    @SerializedName("Coupon")
    public ArrayList<coupan_list> coupanslist = new ArrayList<>();

    public static class coupan_list {

        @SerializedName("id")
        public String id;

        @SerializedName("code")
        public String code;

        @SerializedName("type")
        public String type;

        @SerializedName("price")
        public String price;

        @SerializedName("times")
        public String times;

        @SerializedName("used")
        public String used;

        @SerializedName("start_date")
        public String start_date;

        @SerializedName("end_date")
        public String end_date;

        @SerializedName("status")
        public String status;
    }

}
