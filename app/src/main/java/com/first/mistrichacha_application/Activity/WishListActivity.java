package com.first.mistrichacha_application.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.LatestProductAdapter;
import com.first.mistrichacha_application.Adapter.SubCategoryAdapter;
import com.first.mistrichacha_application.Adapter.WishListAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class WishListActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    APIInterface apiInterface ;
    WishListAdapter latestProductAdapter;
    LinearLayout llMain ;
    SessionManagment sd ;
    ArrayList<LatestProductModel.datalist> al = new ArrayList<>();
    int pos ;
    CardView cardView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabcategory_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(WishListActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        sd = new SessionManagment(WishListActivity.this);

        cardView =(CardView)findViewById(R.id.card);
        cardView.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("WishList");

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(WishListActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void retroWish() {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(WishListActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<LatestProductModel> call = apiInterface.getWishList("Bearer "+sd.getKEY_APITOKEN(),"sort");
                call.enqueue(new Callback<LatestProductModel>() {
                    @Override
                    public void onResponse(Call<LatestProductModel> call, retrofit2.Response<LatestProductModel> response) {
                        al.clear();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        LatestProductModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            al.addAll(pro.productlist);
                            latestProductAdapter = new WishListAdapter(al,WishListActivity.this);
                            recyclerView.setAdapter(latestProductAdapter);
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
            Toast.makeText(WishListActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

   @Override
    protected void onResume() {
        super.onResume();
        retroWish();
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

}
