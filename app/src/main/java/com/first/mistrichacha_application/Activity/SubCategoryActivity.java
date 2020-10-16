package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import retrofit2.Call;
import retrofit2.Callback;

public class SubCategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    String subCategory =  "",slug="";
    SubCategoryAdapter subCategoryAdapter;
    LinearLayout llMain ;
    SearchView searchView ;
    CardView cardView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabcategory_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(SubCategoryActivity.this);
        sd = new SessionManagment(SubCategoryActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        searchView =(SearchView)findViewById(R.id.seachview);
        cardView = (CardView) findViewById(R.id.card);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Your Product Here");

        Intent in = getIntent();
        subCategory =  in.getStringExtra("subCategory");
        slug =  in.getStringExtra("slug");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subCategory);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(SubCategoryActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        retro(slug);

    }

    private void retro(String slug) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(SubCategoryActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SubCategoryModel> call = apiInterface.getCategoryProduct(slug,sd.getLatitude(),sd.getLongitude());
                call.enqueue(new Callback<SubCategoryModel>() {
                    @Override
                    public void onResponse(Call<SubCategoryModel> call, retrofit2.Response<SubCategoryModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        SubCategoryModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            cardView.setVisibility(View.GONE);
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            subCategoryAdapter = new SubCategoryAdapter(pro.productlist,SubCategoryActivity.this);
                            recyclerView.setAdapter(subCategoryAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<SubCategoryModel> call, Throwable t) {
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        try {
            subCategoryAdapter.filter(text);
        }catch (Exception e){

        }
        return false;
    }
}
