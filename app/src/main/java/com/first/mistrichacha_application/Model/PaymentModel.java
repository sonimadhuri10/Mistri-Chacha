package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentModel {

    @SerializedName("success")
    public String success;

    @SerializedName("wallet_amout")
    public String wallet_amount;

    @SerializedName("remaining_amount")
    public String remaining;

    @SerializedName("use_wallet")
    public String use_wallet;

    @SerializedName("data")
    public ArrayList<datalist> dlist = new ArrayList<>();

    public static class datalist {

        @SerializedName("totalQty")
        public String totalQty;

        @SerializedName("id")
        public String id;

        @SerializedName("total")
        public String total;

        @SerializedName("pay_amount")
        public String pay_amount;

        @SerializedName("order_number")
        public String order_number;

        @SerializedName("shipping_name")
        public String shipping_name;

        @SerializedName("shipping_email")
        public String shipping_email;

        @SerializedName("shipping_phone")
        public String shipping_phone;

        @SerializedName("shipping_address")
        public String shipping_address;

        @SerializedName("shipping_cost")
        public String shipping_cost;

        @SerializedName("packing_cost")
        public String packing_cost;

        @SerializedName("tax")
        public String tax;

        @SerializedName("coupon_discount")
        public String coupon_discount;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("status")
        public String status;

        @SerializedName("photo")
        public String photo;

        @SerializedName("method")
        public String method;


    }


    @SerializedName("product")
    public ArrayList<productlist> productlist = new ArrayList<>();

    public static class productlist {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("price")
        public String price;

        @SerializedName("store")
        public String store;

        @SerializedName("qty")
        public String qty;

        @SerializedName("photo")
        public String photo;

        @SerializedName("status")
        public String status;


    }

    @SerializedName("wallet")
    public ArrayList<transaction> transactionlist = new ArrayList<>();

    public static class transaction {

        @SerializedName("amount")
        public String amount;

        @SerializedName("tnx_id")
        public String tnx_id;

        @SerializedName("type")
        public String type;

        @SerializedName("created_at")
        public String created_at;

    }

}
