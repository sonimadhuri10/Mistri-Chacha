package com.first.mistrichacha_application.Networkmanager;


import com.first.mistrichacha_application.Model.CategoryModel;
import com.first.mistrichacha_application.Model.CheckOutModel;
import com.first.mistrichacha_application.Model.FAQ_Model;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.Partner_Model;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Model.PrivacyModel;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.Model.ReviewModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Model.StoreModel;
import com.first.mistrichacha_application.Model.SubCategoryModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by ANDROID on 08-Jan-18.
 */

public interface APIInterface {


    // GET METHODS

    @GET("privacy")
    Call<PrivacyModel> getPrivacyPolicy();

    @GET("about")
    Call<PrivacyModel> getAboutUs();

    @GET("terms")
    Call<PrivacyModel> getTermsAndCondition();

    @GET("faq")
    Call<FAQ_Model> getFAQ();

    @GET("contact")
    Call<PrivacyModel> getContact();

    @GET("Category")
    Call<CategoryModel> getCategory();

    @GET("partners")
    Call<Partner_Model> getPartner();

    @GET("Coupons")
    Call<StoreModel> getCoupans();

    @GET("wallet")
    Call<SignupModel> getWallet(@Header("Authorization") String header);

    @GET("AllNotification")
    Call<SignupModel> getNotification(@Header("Authorization") String header);

    @GET("myOrder")
    Call<PaymentModel> getMyOrder(@Header("Authorization") String header);

    @GET("walletTransection")
    Call<PaymentModel> getWalletTransaction(@Header("Authorization") String header);

    // POST METHODS

    @FormUrlEncoded
    @POST("register")
    Call<SignupModel> getRegister(@Field("name") String name, @Field("phone") String mobileno,
                                     @Field("email") String email, @Field("password") String password,
                                     @Field("password_confirmation") String password_confirmation,
                                     @Field("address") String address,
                                     @Field("city") String city, @Field("zip") String zipcode,
                                     @Field("state") String state, @Field("country") String country,
                                     @Field("device_id") String device_id,@Field("photo") String photo);

    @Multipart
    @POST("updateProfile")
    Call<SignupModel> getUpdateProfile(@Header("Authorization") String header,
                                       @Part MultipartBody.Part file, @Part("photo") RequestBody photo,
                                       @Part("name") RequestBody name,
                                       @Part("phone") RequestBody mobileno, @Part("email") RequestBody email,
                                       @Part("address") RequestBody address,
                                       @Part("city") RequestBody city, @Part("zip") RequestBody zipcode,
                                       @Part("state") RequestBody state, @Part("country") RequestBody country,
                                       @Part("flat_no") RequestBody flat_no, @Part("building") RequestBody building);

    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> getLogin(@Field("email") String email, @Field("password") String password,
                               @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("login/otp")
    Call<SignupModel> getOtpVerify(@Field("phone") String mobileno,@Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("category/product")
    Call<SubCategoryModel> getCategoryProduct(@Field("slug") String slug,
                                              @Field("latitude") String latitude ,
                                              @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("product")
    Call<ProductModel> getproductDetail(@Field("id") String slug);

    @FormUrlEncoded
    @POST("Shop-product")
    Call<LatestProductModel> getShopProducts(@Field("id") String slug,
                                             @Field("latitude") String latitude ,
                                             @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("autosearch/product")
    Call<LatestProductModel> getSearchResult(@Field("slug") String slug,@Field("type") String type,
                                             @Field("latitude") String latitude ,
                                             @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("viewAll")
    Call<LatestProductModel> getViewAll(@Field("name") String name,@Field("latitude") String latitude ,
                                        @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("rating")
    Call<ReviewModel> getAllReview(@Field("id") String id);

    @FormUrlEncoded
    @POST("comment")
    Call<ReviewModel> getAllComments(@Field("id") String id);

    @FormUrlEncoded
    @POST("checkNumber")
    Call<SignupModel> getVerifyNumber(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("forgetPass")
    Call<SignupModel> getForgetPassword(@Header("Authorization") String header, @Field("password") String mobile);

    @FormUrlEncoded
    @POST("wishlist/add")
    Call<SignupModel> addWidhlist(@Header("Authorization") String header, @Field("id") String id);

    @FormUrlEncoded
    @POST("wishlist/remove")
    Call<SignupModel> removeWishlist(@Header("Authorization") String header, @Field("id") String id);

    @FormUrlEncoded
    @POST("wishlists")
    Call<LatestProductModel> getWishList(@Header("Authorization") String header,@Field("date_desc") String key);

    @FormUrlEncoded
    @POST("checkout")
    Call<CheckOutModel> getCheckOut(@Header("Authorization") String header, @Field("id") String id,
                                    @Field("totalQty") String totalQty , @Field("totalPrice") String totalPrice,
                                    @Field("qty") String quantity , @Field("size_price") String size_price,
                                    @Field("color") String color , @Field("size") String size);

    @FormUrlEncoded
    @POST("return/order")
    Call<CheckOutModel> getReturnProduct(@Header("Authorization") String header,
                                          @Field("product_id") String product_id,
                                         @Field("order_id") String order_id );

    @FormUrlEncoded
    @POST("cancel/order")
    Call<CheckOutModel> getCancleproduct(@Header("Authorization") String header,
                                         @Field("order_id") String order_id );

    @FormUrlEncoded
    @POST("payment")
    Call<PaymentModel> getPlaceOrder(@Header("Authorization") String header,
                                     @Field("id") String id,
                                     @Field("totalQty") String totalQty,
                                     @Field("totalPrice") String totalPrice,
                                     @Field("qty") String quantity ,
                                     @Field("size_price") String size_price ,
                                     @Field("email") String email ,
                                     @Field("name") String name ,
                                     @Field("shipping_cost") String shipping_cost ,
                                     @Field("packing_cost") String packing_cost ,
                                     @Field("customer_country") String customer_country ,
                                     @Field("dp") String dp ,
                                     @Field("vendor_shipping_id") String vendor_shipping_id ,
                                     @Field("vendor_packing_id") String vendor_packing_id ,
                                     @Field("building") String building ,
                                     @Field("flat_no") String flat_no,
                                     @Field("method") String method ,
                                     @Field("shipping") String shipping,
                                     @Field("pickup_location") String pickup_location ,
                                     @Field("address") String address,
                                     @Field("city") String city ,
                                     @Field("zip") String zip,
                                     @Field("shipping_name") String shipping_name ,
                                     @Field("shipping_email") String shipping_email,
                                     @Field("shipping_phone") String shipping_phone , @Field("shipping_address") String shipping_address,
                                     @Field("shipping_country") String shipping_country ,
                                     @Field("shipping_city") String shipping_city,
                                     @Field("shipping_zip") String shipping_zip ,
                                     @Field("order_notes") String order_notes,
                                     @Field("coupon_code") String coupon_code ,
                                     @Field("coupon_discount") String coupon_discount,
                                     @Field("phone") String phone,
                                     @Field("is_wallet") String is_wallet,
                                     @Field("wallet_amount") String wallet_amount,
                                     @Field("client_gst") String client_gst,
                                     @Field("color") String color,
                                     @Field("size") String size ,
                                     @Field("tax") String tax);

    @FormUrlEncoded
    @POST("carts/coupon/check")
    Call<PaymentModel> getCoupanCheck(@Header("Authorization") String header, @Field("code") String code,
                                    @Field("total") String total , @Field("shipping_cost") String shipping_cost);

    @FormUrlEncoded
    @POST("myOrder/details")
    Call<PaymentModel> getMyOrderDetails(@Header("Authorization") String header,
                                         @Field("id") String id);


    @FormUrlEncoded
    @POST("addWallet")
    Call<PaymentModel> AddWallet(@Header("Authorization") String header,
                                         @Field("amount") String amount);
 @FormUrlEncoded
    @POST("pay_wallet")
    Call<PaymentModel> PayWallet(@Header("Authorization") String header,
                                         @Field("amount") String amount);

    @FormUrlEncoded
    @POST("comment/store")
    Call<PaymentModel> giveComment(@Header("Authorization") String header, @Field("product_id") String id ,
                                   @Field("text") String text);

    @FormUrlEncoded
    @POST("review/add")
    Call<PaymentModel> giveReview(@Header("Authorization") String header,
                                  @Field("product_id") String id ,
                                  @Field("rating") String text ,
                                  @Field("review") String review);
    @FormUrlEncoded
    @POST("latestProduct")
    Call<LatestProductModel> getLatestProducts(@Field("latitude") String latitude ,
                                               @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("discountProducts")
    Call<LatestProductModel> getDiscountproducts(@Field("latitude") String latitude ,
                                                 @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("trendingProducts")
    Call<LatestProductModel> getTrendingproducts(@Field("latitude") String latitude ,
                                                 @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("featureProducts")
    Call<LatestProductModel> getFeaturedproducts(@Field("latitude") String latitude ,
                                                 @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("wholeSale")
    Call<LatestProductModel> getWholesaleProducts(@Field("latitude") String latitude ,
                                                  @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("home")
    Call<FinalCategoryModel> getHome(@Field("latitude") String latitude ,
                                     @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("allStore")
    Call<StoreModel> getAllStore(@Field("latitude") String latitude ,
                                 @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("changePassword")
    Call<SignupModel> getChnagePassword(@Header("Authorization") String header,
                                        @Field("password") String password ,
                                        @Field("Oldpassword") String Oldpassword);
}

