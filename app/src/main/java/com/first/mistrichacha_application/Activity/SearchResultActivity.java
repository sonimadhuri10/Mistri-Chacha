package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.LatestProductAdapter;
import com.first.mistrichacha_application.Adapter.SubCategoryAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.SubCategoryModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    String subCategory =  "",slug="";
    LatestProductAdapter subCategoryAdapter;
    LinearLayout llMain ;
    ArrayList<LatestProductModel.datalist> al = new ArrayList<>();
    EditText etSearch ;
    ImageView imgSearch ;
    LinearLayout llImage , llEdit ,llHeader ;
    TextView tvHeader ;
    String search = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(SearchResultActivity.this);
        sd = new SessionManagment(SearchResultActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

        Intent in = getIntent();
        subCategory =  in.getStringExtra("subCategory");
        slug =  in.getStringExtra("slug");

        final Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle("");
         tvHeader = findViewById(R.id.pg2_header_footer);
         llEdit = findViewById(R.id.llEdit);
         llImage = findViewById(R.id.llImage);
         llHeader = findViewById(R.id.llheaderview);

        tvHeader.setText(slug);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(SearchResultActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        imgSearch.setOnClickListener(this);
        llImage.setOnClickListener(this);

        retro(slug);

    }

    private void retro(String slug) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(SearchResultActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<LatestProductModel> call = apiInterface.getSearchResult(slug,"",sd.getLatitude(),sd.getLongitude());
                call.enqueue(new Callback<LatestProductModel>() {
                    @Override
                    public void onResponse(Call<LatestProductModel> call, retrofit2.Response<LatestProductModel> response) {
                        al.clear();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        LatestProductModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            al.addAll(pro.productlist);
                            subCategoryAdapter = new LatestProductAdapter(al,SearchResultActivity.this);
                            recyclerView.setAdapter(subCategoryAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<LatestProductModel> call, Throwable t) {
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
            case R.id.imgSearch:
                subCategory = etSearch.getText().toString().trim();

                if(subCategory.equals("")){
                    etSearch.setError("Please Enter Value For Search");
                }else{
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    llMain.setBackgroundResource(R.color.colorWhite);
                    retro(subCategory);
                }
                break;
            case R.id.llImage:
                llHeader.setVisibility(View.GONE);
                llEdit.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }
}
