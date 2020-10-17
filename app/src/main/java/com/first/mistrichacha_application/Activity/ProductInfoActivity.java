package com.first.mistrichacha_application.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.ColorAdapter;
import com.first.mistrichacha_application.Adapter.RelatedProductAdapter;
import com.first.mistrichacha_application.Adapter.SizeAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.DatabaseManager.DatabaseHelper;
import com.first.mistrichacha_application.Interface.ColorInterface;
import com.first.mistrichacha_application.Interface.SizeInterface;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductInfoActivity extends AppCompatActivity implements
        ViewPagerEx.OnPageChangeListener,BaseSliderView.OnSliderClickListener,
        View.OnClickListener , ColorInterface , SizeInterface {

    private HashMap<Integer, String> url_maps;
    ConnectionDetector cd ;
    SessionManagment sd;
    ArrayList <ProductModel.gallerytinfo>sliderList ;
    APIInterface apiInterface ;
    private ShimmerFrameLayout mShimmerViewContainer;
    TextView tvName , tvPreiviousPrice , tvprice , tvType , tvStock , tvSoldBy ,
             tvWarrunty , tvBrandName , tvEstimatedTime , tvSku , tvSizeAvailability , tvColorAvaialbility;
    WebView webView;
    String id = "",like = "";
    SliderLayout sliderLayout ;
    RecyclerView recyclerView , recyclerView_color , recyclerView_size;
    RelatedProductAdapter relatedProductAdapter ;
    ColorAdapter colorAdapter ;
    SizeAdapter sizeAdapter ;
    ImageView imgLike ,imgDislike ;
    LinearLayout llDiscription , llRelated , llColor ,llSize, llUseComment ,llTerms ,
                 llViewCcomments , llViewAllTerms , llViewAllRefund , llWriteCommnets ,
                 llReview , llViewReview , llWriteReview;
    ImageView imgdisup , imgdisDown  ,imgRelup , imgRelDown ,
            imgColorDown ,imgColorUp , imgUserDown , imgUserUp ,
            imgTermsDown , imgTermsUp , imgReviewup , imgReviewDown ,imgSizeDown , imgSizeUp ;
    Button btnAddtoCart , btnBuyNow ;
    DatabaseHelper mydb;
    String name = "", price = "", description= "",category = "",photo="";
    Dialog dialog;
    private  int pos;
    String quan = "";
    String policy = "", vendor = "" , ship="", warranty = "", brand="",sku="",
            selectedColor = "",checkcolor="", selectedSize = "", checksize="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productinfo_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Detail");

        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mydb = new DatabaseHelper(ProductInfoActivity.this);

        tvName=(TextView)findViewById(R.id.tvName);
        tvPreiviousPrice=(TextView)findViewById(R.id.tvPriviousPrice);
        tvprice=(TextView)findViewById(R.id.tvPrice);
        tvSoldBy=(TextView)findViewById(R.id.tvSoldBy);
        tvType=(TextView)findViewById(R.id.tvType);
        tvStock=(TextView)findViewById(R.id.tvStock);
        tvWarrunty=(TextView)findViewById(R.id.tvWarrunty);
        tvEstimatedTime=(TextView)findViewById(R.id.tvEstimatedTime);
        tvBrandName=(TextView)findViewById(R.id.tvBrandName);
        tvSku=(TextView)findViewById(R.id.tvSku);
        tvSizeAvailability=(TextView)findViewById(R.id.tvSizeAvailability);
        tvColorAvaialbility=(TextView)findViewById(R.id.tvColorAvailability);
        webView=(WebView) findViewById(R.id.web1);
        sliderLayout = (SliderLayout)findViewById(R.id.productSlider);
        llDiscription = (LinearLayout)findViewById(R.id.llDiscription);
        llRelated = (LinearLayout)findViewById(R.id.llRelated);
        llColor = (LinearLayout)findViewById(R.id.llcolor);
        llSize = (LinearLayout)findViewById(R.id.llsize);
        llUseComment = (LinearLayout)findViewById(R.id.lluserComment);
        llTerms = (LinearLayout)findViewById(R.id.lltermsandPolicy);
        llViewCcomments = (LinearLayout)findViewById(R.id.llViewAllComments);
        llViewAllTerms = (LinearLayout)findViewById(R.id.llViewAllTerms);
        llViewAllRefund = (LinearLayout)findViewById(R.id.llViewAllRefund);
        llWriteCommnets = (LinearLayout)findViewById(R.id.llWriteComments);
        llReview = (LinearLayout)findViewById(R.id.lluserReview);
        llViewReview = (LinearLayout)findViewById(R.id.llViewAllReviews);
        llWriteReview = (LinearLayout)findViewById(R.id.llWriteReviews);

        imgLike =(ImageView)findViewById(R.id.imgLike);
        imgDislike =(ImageView)findViewById(R.id.imgDislike);

        btnBuyNow=(Button)findViewById(R.id.btnButNow);
        btnAddtoCart=(Button)findViewById(R.id.btnAddToCart);

        imgdisDown = (ImageView)findViewById(R.id.img_dis_down);
        imgdisup = (ImageView)findViewById(R.id.img_dis_up);
        imgRelDown = (ImageView)findViewById(R.id.img_related_down);
        imgRelup = (ImageView)findViewById(R.id.img_related_up);
        imgColorDown = (ImageView)findViewById(R.id.img_color_down);
        imgColorUp = (ImageView)findViewById(R.id.img_color_up);
        imgUserDown = (ImageView)findViewById(R.id.img_user_down);
        imgUserUp = (ImageView)findViewById(R.id.img_user_up);
        imgTermsUp = (ImageView)findViewById(R.id.img_terms_up);
        imgTermsDown = (ImageView)findViewById(R.id.img_terms_down);
        imgReviewup = (ImageView)findViewById(R.id.img_review_up);
        imgReviewDown = (ImageView)findViewById(R.id.img_review_down);
        imgSizeUp = (ImageView)findViewById(R.id.img_size_up);
        imgSizeDown = (ImageView)findViewById(R.id.img_size_down);

        recyclerView = (RecyclerView)findViewById(R.id.recycleview_relatedProducts);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductInfoActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView_color = (RecyclerView)findViewById(R.id.recycleview_color);
        recyclerView_color.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(ProductInfoActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView_color.setLayoutManager(layoutManager1);
        recyclerView_color.setItemAnimator(new DefaultItemAnimator());

        recyclerView_size = (RecyclerView)findViewById(R.id.recycleview_size);
        recyclerView_size.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(ProductInfoActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView_size.setLayoutManager(layoutManager2);
        recyclerView_size.setItemAnimator(new DefaultItemAnimator());

        sliderList = new ArrayList<>();
        Intent in = getIntent();
        id = in.getStringExtra("product_id");
        like = in.getStringExtra("like");
        if(like.equals("favourite")){
            pos = in.getIntExtra("position",0);
        }

        if(like.equals("favourite")){
            imgDislike.setVisibility(View.VISIBLE);
            imgLike.setVisibility(View.GONE);
        }

        imgdisDown.setOnClickListener(this);
        imgdisup.setOnClickListener(this);
        imgRelDown.setOnClickListener(this);
        imgRelup.setOnClickListener(this);
        imgColorDown.setOnClickListener(this);
        imgColorUp.setOnClickListener(this);
        imgUserUp.setOnClickListener(this);
        imgUserDown.setOnClickListener(this);
        imgTermsDown.setOnClickListener(this);
        imgTermsUp.setOnClickListener(this);
        imgReviewDown.setOnClickListener(this);
        imgReviewup.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        imgDislike.setOnClickListener(this);
        imgSizeDown.setOnClickListener(this);
        imgSizeUp.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        btnAddtoCart.setOnClickListener(this);
        llViewAllRefund.setOnClickListener(this);
        llViewAllTerms.setOnClickListener(this);
        llViewCcomments.setOnClickListener(this);
        llWriteCommnets.setOnClickListener(this);
        llViewReview.setOnClickListener(this);
        llWriteReview.setOnClickListener(this);

       retro(id);

    }

    private void retro(String id) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(ProductInfoActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<ProductModel> call = apiInterface.getproductDetail(id);
                call.enqueue(new Callback<ProductModel>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<ProductModel> call, retrofit2.Response<ProductModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        final ProductModel pro = response.body();

                        name = pro.productlist.get(0).name ;
                        price = pro.productlist.get(0).price ;
                        description = pro.productlist.get(0).details.replaceAll("\\<.*?\\>", "");
                        category = pro.productlist.get(0).category_id ;
                        photo = pro.productlist.get(0).thumbnail ;
                        policy = pro.productlist.get(0).policy;

                       tvName.setText(pro.productlist.get(0).name);
                       tvPreiviousPrice.setText(pro.productlist.get(0).previous_price);
                       tvPreiviousPrice.setPaintFlags(tvPreiviousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                       tvprice.setText(pro.productlist.get(0).price);
                       tvType.setText(pro.productlist.get(0).type);

                       vendor = pro.productlist.get(0).vendorname ;
                       tvSoldBy.setText("Sold By : "+vendor);

                       sku = pro.productlist.get(0).sku ;
                       tvSku.setText("Product SKU : "+sku);

                      warranty = pro.warranty ;
                       if(!warranty.equals("")){
                           tvWarrunty.setText("Warranty : "+warranty);
                       }else{
                           tvWarrunty.setText("Warranty : "+"None");
                       }

                       brand = pro.brand ;
                       if(!brand.equals("")){
                           tvBrandName.setText(brand);
                       }

                       ship=pro.productlist.get(0).ship ;
                       tvEstimatedTime.setText("Estimated Shipping Time : "+ship);

                       if(pro.productlist.get(0).stock.equals("")){
                           quan = "0";
                           tvStock.setText("Currently Not Available");
                           tvStock.setTextColor(Color.parseColor("#FF0000"));
                       }else{
                           quan = "1";
                           tvStock.setText("In Stock");
                           tvStock.setTextColor(Color.parseColor("#008000"));
                       }

                        if(pro.gallerylist.size() == 0){

                        }else{
                            sliderList.addAll(pro.gallerylist);
                             url_maps = new HashMap<Integer, String>();
                            String urlString = "";
                            for (int i = 0; i < sliderList.size(); i++) {
                                urlString = sliderList.get(i).photo.replaceAll(" ", "%20");
                                url_maps.put(i, urlString);
                            }

                            Map<Integer, String> map = new TreeMap<Integer, String>(url_maps);

                            for (int name : map.keySet()) {
                             TextSliderView textSliderView = new TextSliderView(ProductInfoActivity.this);
                                    textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(ProductInfoActivity.this);                                ;
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra",sliderList.get(0).photo);
                                    sliderLayout.addSlider(textSliderView);
                            }
                                sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                sliderLayout.setDuration(3000);
                                sliderLayout.addOnPageChangeListener(ProductInfoActivity.this);
                        }

                        String s1 = pro.productlist.get(0).details.replaceAll("\\<.*?\\>", "");
                        String text1;
                        text1 = "<html><body><p align=\"justify\">";
                        text1 += s1;
                        text1 += "</p></body></html>";

                        webView.loadData(text1, "text/html", "utf-8(\\\"color\\\", \\\"blue\\\");");
                        WebSettings settings = webView.getSettings();
                        settings.setTextZoom(75);

                        webView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return true;
                            }
                        });
                        webView.setLongClickable(false);

                        relatedProductAdapter = new RelatedProductAdapter(pro.relatedProductList, ProductInfoActivity.this);
                        recyclerView.setAdapter(relatedProductAdapter);

                        if(pro.colorlist.size()!=0){
                            checkcolor = "select";
                            colorAdapter = new ColorAdapter(pro.colorlist, ProductInfoActivity.this);
                            recyclerView_color.setAdapter(colorAdapter);
                        }else{
                            recyclerView_color.setVisibility(View.GONE);
                            tvColorAvaialbility.setVisibility(View.VISIBLE);
                        }

                        if(pro.slist.size()!=0){
                            checksize = "select";
                            sizeAdapter = new SizeAdapter(pro.slist, ProductInfoActivity.this);
                            recyclerView_size.setAdapter(sizeAdapter);
                        }else{
                            recyclerView_size.setVisibility(View.GONE);
                            tvSizeAvailability.setVisibility(View.VISIBLE);
                        }


                    }
                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Toast.makeText(ProductInfoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        catch (Exception e){
        e.printStackTrace();
        }


    }

    private void add_wishlist(String id) {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(ProductInfoActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SignupModel> call = apiInterface.addWidhlist("Bearer "+sd.getKEY_APITOKEN(),id);
                call.enqueue(new Callback<SignupModel>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        SignupModel pro = response.body();
                        if(pro.success.equals("added In wish list")){
                            imgLike.setVisibility(View.GONE);
                            imgDislike.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductInfoActivity.this, "Successfully , added to wishlist", Toast.LENGTH_SHORT).show();
                        }else if(pro.success.equals("already In wish list")){
                            Toast.makeText(ProductInfoActivity.this, "Already added to wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<SignupModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){

        }


    }

    private void remove_wishlist(String id) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(ProductInfoActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SignupModel> call = apiInterface.removeWishlist("Bearer "+sd.getKEY_APITOKEN(),id);
                call.enqueue(new Callback<SignupModel>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        SignupModel pro = response.body();
                        if(pro.success.equals("Remove from list")){
                            imgLike.setVisibility(View.VISIBLE);
                            imgDislike.setVisibility(View.GONE);
                            Toast.makeText(ProductInfoActivity.this, "Successfully , remove from wishlist", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);

                        }
                    }
                    @Override
                    public void onFailure(Call<SignupModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.imgLike:
              if(sd.getLOGIN_STATUS().equals("skip")){
                  showdialoglogin();
              }else{
                  mShimmerViewContainer.setVisibility(View.VISIBLE);
                  add_wishlist(id);
              }
              break ;
          case R.id.img_dis_down:
              imgdisDown.setVisibility(View.GONE);
              imgdisup.setVisibility(View.VISIBLE);
              llDiscription.setVisibility(View.VISIBLE);
              break;
          case R.id.img_dis_up:
              imgdisDown.setVisibility(View.VISIBLE);
              imgdisup.setVisibility(View.GONE);
              llDiscription.setVisibility(View.GONE);
              break;
          case R.id.img_related_down:
              imgRelDown.setVisibility(View.GONE);
              imgRelup.setVisibility(View.VISIBLE);
              llRelated.setVisibility(View.VISIBLE);
              break;
          case R.id.img_related_up:
              imgRelDown.setVisibility(View.VISIBLE);
              imgRelup.setVisibility(View.GONE);
              llRelated.setVisibility(View.GONE);
             break;
          case R.id.img_color_down:
              imgColorDown.setVisibility(View.GONE);
              imgColorUp.setVisibility(View.VISIBLE);
              llColor.setVisibility(View.VISIBLE);
              break;
          case R.id.img_color_up:
              imgColorDown.setVisibility(View.VISIBLE);
              imgColorUp.setVisibility(View.GONE);
              llColor.setVisibility(View.GONE);
              break;
          case R.id.img_user_down:
              imgUserDown.setVisibility(View.GONE);
              imgUserUp.setVisibility(View.VISIBLE);
              llUseComment.setVisibility(View.VISIBLE);
              break;
          case R.id.img_user_up:
              imgUserDown.setVisibility(View.VISIBLE);
              imgUserUp.setVisibility(View.GONE);
              llUseComment.setVisibility(View.GONE);
              break;
          case R.id.img_size_down:
              imgSizeDown.setVisibility(View.GONE);
              imgSizeUp.setVisibility(View.VISIBLE);
              llSize.setVisibility(View.VISIBLE);
              break;
          case R.id.img_size_up:
              imgSizeDown.setVisibility(View.VISIBLE);
              imgSizeUp.setVisibility(View.GONE);
              llSize.setVisibility(View.GONE);
              break;
          case R.id.img_terms_down:
              imgTermsDown.setVisibility(View.GONE);
              imgTermsUp.setVisibility(View.VISIBLE);
              llTerms.setVisibility(View.VISIBLE);
              break;
          case R.id.img_terms_up:
              imgTermsDown.setVisibility(View.VISIBLE);
              imgTermsUp.setVisibility(View.GONE);
              llTerms.setVisibility(View.GONE);
              break;
          case R.id.img_review_down:
              imgReviewDown.setVisibility(View.GONE);
              imgReviewup.setVisibility(View.VISIBLE);
              llReview.setVisibility(View.VISIBLE);
              break;
          case R.id.img_review_up:
              imgReviewDown.setVisibility(View.VISIBLE);
              imgReviewup.setVisibility(View.GONE);
              llReview.setVisibility(View.GONE);
              break;
          case R.id.imgDislike:
              mShimmerViewContainer.setVisibility(View.VISIBLE);
              remove_wishlist(id);
              break;
          case R.id.btnAddToCart:
              if(quan.equals("0")){
                  showdialog();
              }else{
                  if(checkcolor.equals("select") && selectedColor.equals("")){
                      Toast.makeText(ProductInfoActivity.this, "OOPS ! You Did Not Select Color", Toast.LENGTH_SHORT).show();
                  }else if(checksize.equals("select") && selectedSize.equals("")){
                      Toast.makeText(ProductInfoActivity.this, "OOPS ! You Did Not Select Size", Toast.LENGTH_SHORT).show();
                  } else if(mydb.ifBothExists(id,name) == true){
                      addtocart(mydb.quantity(id));
                  }else{
                      addtocart("1");
                  }
              }
              break;
          case R.id.btnButNow:
              if(quan.equals("0")){
                  showdialog();
              }else{
                  if(checkcolor.equals("select") && selectedColor.equals("")){
                      Toast.makeText(ProductInfoActivity.this, "OOPS ! You Did Not Select Color", Toast.LENGTH_SHORT).show();
                  }else if(checksize.equals("select") && selectedSize.equals("")){
                      Toast.makeText(ProductInfoActivity.this, "OOPS ! You Did Not Select Size", Toast.LENGTH_SHORT).show();
                  } else if(mydb.ifBothExists(id,name) == true){
                      buyNow(mydb.quantity(id));
                  }else{
                      buyNow("1");
                  }
              }
              break;
          case R.id.llViewAllComments:
              Intent in11= new Intent(ProductInfoActivity.this,CommentsActivity.class);
              in11.putExtra("id",id);
              in11.putExtra("name","Comments");
              startActivity(in11);
              overridePendingTransition(R.anim.left_in, R.anim.left_out);
              break;
          case R.id.llViewAllTerms:
              Intent in1 = new Intent(ProductInfoActivity.this,BuyAndReturnpolicy.class);
              in1.putExtra("data",policy);
              startActivity(in1);
              overridePendingTransition(R.anim.left_in, R.anim.left_out);
              break;
          case R.id.llViewAllRefund:

              break;
          case R.id.llWriteComments:
              if(sd.getLOGIN_STATUS().equals("true")){
                  Intent in1w= new Intent(ProductInfoActivity.this,GiveFeedBAckActivity.class);
                  in1w.putExtra("id",id);
                  in1w.putExtra("name","Comments");
                  startActivity(in1w);
                  overridePendingTransition(R.anim.left_in, R.anim.left_out);
              }else{
                  showdialoglogin();
              }

              break;
          case R.id.llViewAllReviews:
              Intent in111= new Intent(ProductInfoActivity.this,CommentsActivity.class);
              in111.putExtra("id",id);
              in111.putExtra("name","Reviews");
              startActivity(in111);
              overridePendingTransition(R.anim.left_in, R.anim.left_out);
              break;
          case R.id.llWriteReviews:
              if(sd.getLOGIN_STATUS().equals("true")){
                  Intent in1w= new Intent(ProductInfoActivity.this,GiveFeedBAckActivity.class);
                  in1w.putExtra("id",id);
                  in1w.putExtra("name","Reviews");
                  startActivity(in1w);
                  overridePendingTransition(R.anim.left_in, R.anim.left_out);
              }else{
                  showdialoglogin();

              }
              break;
          default:
              break;
      }
    }

    public void showdialoglogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductInfoActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ProductInfoActivity.this).inflate(R.layout.login_dialog_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvlogin , tvSignup ,tvheader ;
        tvlogin=(TextView)dialogView.findViewById(R.id.tvLogin);
        tvSignup=(TextView)dialogView.findViewById(R.id.tvSignup);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Sorry , You are not logged in , So Login firstly");
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(ProductInfoActivity.this,LoginActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(ProductInfoActivity.this,SignupActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

    }

    public void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductInfoActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ProductInfoActivity.this).inflate(R.layout.outofstock_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvheader ,tvOk ;
        tvOk=(TextView)dialogView.findViewById(R.id.tvOk);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Sorry , This item is currently out of stock");
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });

    }

    public  void addtocart(String qty){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_quantity_layout);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100;
        wlp.x = 100;
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.windowAnimations = R.style.DialogAnimation;
        window.setAttributes(wlp);

        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        final TextView tvname = (TextView) dialog.findViewById(R.id.tvproductName);
        final TextView tvPrice = (TextView) dialog.findViewById(R.id.tvPrice);
        final TextView tvCount = (TextView) dialog.findViewById(R.id.tvCount);
        final TextView tvAddToCart = (TextView) dialog.findViewById(R.id.tvAddTocart);
        tvAddToCart.setText("Add To Basket");

        final TextView tvBuyNow = (TextView) dialog.findViewById(R.id.tvBuyNow);
        final TextView tvTotalPrice = (TextView) dialog.findViewById(R.id.tvTotalPrice);
        final ImageView img = (ImageView) dialog.findViewById(R.id.imgPro);
        LinearLayout ll_increment = (LinearLayout)dialog.findViewById(R.id.linear_increment);
        LinearLayout ll_decrement = (LinearLayout)dialog.findViewById(R.id.linear_decrement);

        tvAddToCart.setVisibility(View.VISIBLE);
        tvname.setText(name);
        tvPrice.setText("Rs. "+price);
        tvTotalPrice.setText(price);
        tvCount.setText(qty);
        Double d = Double.parseDouble(price)  * Double.parseDouble(qty) ;
        tvTotalPrice.setText(String.valueOf(d));
        Picasso.with(ProductInfoActivity.this).load(photo).into(img);
        ll_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tvCount.getText().toString());
                if (count > 1) {
                    String str_value = tvCount.getText().toString().trim();
                    int str_qunat = Integer.parseInt(str_value) - 1;
                    tvCount.setText(String.valueOf(str_qunat));
                    Double d = Double.parseDouble(price)  * str_qunat ;
                    tvTotalPrice.setText(String.valueOf(d));
                }
            }
        });

        ll_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_value = tvCount.getText().toString().trim();
                int str_qunat = Integer.parseInt(str_value) + 1;
                tvCount.setText(String.valueOf(str_qunat));
                Double d = Double.parseDouble(price)  * str_qunat ;
                tvTotalPrice.setText(String.valueOf(d));
            }
        });

        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mydb.ifBothExists(id,name) == true){
                    mydb.updateData(id,name,price,tvCount.getText().toString().trim(),description,category,photo,selectedColor,selectedSize);
                    Toast.makeText(ProductInfoActivity.this, "Successfully Update Your Cart", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    mydb.insertData(id,name,price,tvCount.getText().toString().trim(),description,category,photo,selectedColor,selectedSize);
                    Toast.makeText(ProductInfoActivity.this, "Successfully Added To Cart", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }

    public void buyNow(String qty){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_quantity_layout);
        dialog.setCancelable(true);

        Window window1 = dialog.getWindow();
        WindowManager.LayoutParams wlp1 = window1.getAttributes();

        wlp1.gravity = Gravity.BOTTOM;
        wlp1.y = 100;
        wlp1.x = 100;
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp1.windowAnimations = R.style.DialogAnimation;
        window1.setAttributes(wlp1);

        dialog.show();

        DisplayMetrics displayMetrics1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
        WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
        layoutParams1.copyFrom(dialog.getWindow().getAttributes());
        layoutParams1.width = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(layoutParams1);

        final TextView tvname1 = (TextView) dialog.findViewById(R.id.tvproductName);
        final TextView tvPrice1 = (TextView) dialog.findViewById(R.id.tvPrice);
        final TextView tvCount1 = (TextView) dialog.findViewById(R.id.tvCount);
        final TextView tvAddToCart1 = (TextView) dialog.findViewById(R.id.tvAddTocart);

        tvAddToCart1.setText("Buy Now");
        final TextView tvBuyNow1 = (TextView) dialog.findViewById(R.id.tvBuyNow);
        final TextView tvTotalPrice1 = (TextView) dialog.findViewById(R.id.tvTotalPrice);
        final ImageView img1 = (ImageView) dialog.findViewById(R.id.imgPro);
        LinearLayout ll_increment1 = (LinearLayout)dialog.findViewById(R.id.linear_increment);
        LinearLayout ll_decrement1 = (LinearLayout)dialog.findViewById(R.id.linear_decrement);

        tvAddToCart1.setVisibility(View.VISIBLE);
        tvname1.setText(name);
        tvPrice1.setText("Rs. "+price);
        tvCount1.setText(qty);
        Double d = Double.parseDouble(price)  * Double.parseDouble(qty) ;
        tvTotalPrice1.setText(String.valueOf(d));
        Picasso.with(ProductInfoActivity.this).load(photo).into(img1);


        ll_decrement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tvCount1.getText().toString());
                if (count > 1) {
                    String str_value = tvCount1.getText().toString().trim();
                    int str_qunat = Integer.parseInt(str_value) - 1;
                    tvCount1.setText(String.valueOf(str_qunat));
                    Double d = Double.parseDouble(price)  * str_qunat ;
                    tvTotalPrice1.setText(String.valueOf(d));
                }
            }
        });

        ll_increment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_value = tvCount1.getText().toString().trim();
                int str_qunat = Integer.parseInt(str_value) + 1;
                tvCount1.setText(String.valueOf(str_qunat));
                Double d = Double.parseDouble(price)  * str_qunat ;
                tvTotalPrice1.setText(String.valueOf(d));
            }
        });

        tvAddToCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mydb.ifBothExists(id,name) == true){
                    mydb.updateData(id,name,price,tvCount1.getText().toString().trim(),description,category,photo,selectedColor,selectedSize);
                    Toast.makeText(ProductInfoActivity.this, "Successfully Update Your Cart", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ProductInfoActivity.this, DrawerActivity.class);
                    in.putExtra("fragment","cart");
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    dialog.dismiss();

                }else{
                    mydb.insertData(id,name,price,tvCount1.getText().toString().trim(),description,category,photo,selectedColor,selectedSize);
                    Toast.makeText(ProductInfoActivity.this, "Successfully Added To Cart", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ProductInfoActivity.this, DrawerActivity.class);
                    in.putExtra("fragment","cart");
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    dialog.dismiss();
                }

            }
        });

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent in = new Intent(ProductInfoActivity.this, FullImageActivity.class);
        in.putExtra("image",sliderList.get(sliderLayout.getCurrentPosition()).photo.replaceAll(" ", "%20"));
        startActivity(in);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void selectcolorbyyou(String color) {
        selectedColor = color ;
    }

    @Override
    public void selectedSizeByYou(String size) {
     selectedSize = size;
    }
}
