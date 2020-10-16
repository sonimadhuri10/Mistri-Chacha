package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Partner_Model {

    @SerializedName("partners")
    public ArrayList<datalist> partnerlist = new ArrayList<>();

    public static class datalist {

        @SerializedName("id")
        public String id;

        @SerializedName("photo")
        public String photo;

        @SerializedName("link")
        public String link;

    }
}