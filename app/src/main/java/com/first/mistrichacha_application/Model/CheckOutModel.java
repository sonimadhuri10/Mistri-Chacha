package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckOutModel {

    @SerializedName("success")
    public String success;

    @SerializedName("massage")
    public String massage;

    @SerializedName("totalPrice")
    public String totalPrice;

    @SerializedName("tax")
    public String tax;

    @SerializedName("vendor_shipping_id")
    public String vendor_shipping_id;

    @SerializedName("vendor_packing_id")
    public String vendor_packing_id;

    @SerializedName("dp")
    public String dp;

    @SerializedName("shipping_data")
    public ArrayList<shipList> shipping_list = new ArrayList<>();

    public static class shipList {

        @SerializedName("title")
        public String title;

        @SerializedName("price")
        public String price;

    }

    @SerializedName("package_data")
    public ArrayList<packlist> packing_list = new ArrayList<>();

    public static class packlist {

        @SerializedName("title")
        public String title;

        @SerializedName("price")
        public String price;

    }

}
