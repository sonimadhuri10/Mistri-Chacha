package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FAQ_Model {

    @SerializedName("faq")
    public ArrayList<datalist> faqdata = new ArrayList<>();

    public static class datalist {

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("details")
        public String details;

        @SerializedName("status")
        public String status;

    }
}
