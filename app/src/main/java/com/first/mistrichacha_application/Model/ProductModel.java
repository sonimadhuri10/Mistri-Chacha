package com.first.mistrichacha_application.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductModel {

    @SerializedName("product")
    public ArrayList<productinfo> productlist = new ArrayList<>();

    @SerializedName("brand")
    public String brand;

    @SerializedName("warranty")
    public String warranty;

    public static class productinfo {

        @SerializedName("id")
        public String id;

        @SerializedName("product_type")
        public String product_type;

        @SerializedName("sku")
        public String sku;

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

        @SerializedName("type")
        public String type;

        @SerializedName("details")
        public String details;

        @SerializedName("discount")
        public String discount;

        @SerializedName("policy")
        public String policy;

        @SerializedName("vendorname")
        public String vendorname;

        @SerializedName("ship")
        public String ship;

        @SerializedName("color")
        public ArrayList<String> colorlist = new ArrayList<>();



    }

    @SerializedName("size")
    public ArrayList<sizelist> slist = new ArrayList<>();

    public static class sizelist {
        @SerializedName("a")
        public String a;

    }

    @SerializedName("color")
    public ArrayList<colorlisting> colorlist = new ArrayList<>();

    public static class colorlisting {
        @SerializedName("color")
        public String color;

    }


    @SerializedName("Gallery")
    public ArrayList<gallerytinfo> gallerylist = new ArrayList<>();

    public static class gallerytinfo {

        @SerializedName("product_id")
        public String id;

        @SerializedName("photo")
        public String photo;

    }

    @SerializedName("vendors")
    public ArrayList<relatedproductInfo> relatedProductList = new ArrayList<>();

    public static class relatedproductInfo {

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

        @SerializedName("type")
        public String type;

        @SerializedName("details")
        public String details;

        @SerializedName("discount")
        public String discount;

    }

}
