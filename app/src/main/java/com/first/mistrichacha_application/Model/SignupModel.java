package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SignupModel {

    @SerializedName("success")
    public String success;

    @SerializedName("otp")
    public String otp;

    @SerializedName("data")
    public ArrayList<datalist> userdata = new ArrayList<>();

    public static class datalist {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("phone")
        public String phone;

        @SerializedName("email")
        public String email;

        @SerializedName("city")
        public String city;

        @SerializedName("zip")
        public String zip;

        @SerializedName("state")
        public String state;

        @SerializedName("country")
        public String country;

        @SerializedName("address")
        public String address;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("wallet_amount")
        public String wallet_amount;

        @SerializedName("photo")
        public String photo;

        @SerializedName("building")
        public String building;

        @SerializedName("flat_no")
        public String flat_no;

        @SerializedName("api_token")
        public String api_token;


    }

    @SerializedName("notfication")
    public ArrayList<notlist> notifications = new ArrayList<>();

    public static class notlist {

        @SerializedName("body")
        public String body;

        @SerializedName("title")
        public String title;

        @SerializedName("icon")
        public String icon;

        @SerializedName("created_at")
        public String created_at;


    }


}