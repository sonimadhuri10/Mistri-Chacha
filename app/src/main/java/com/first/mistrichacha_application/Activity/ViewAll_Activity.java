package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.LatestProductAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewAll_Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    String subCategory =  "";
    LatestProductAdapter viewAllAdapter;
    LinearLayout llMain ;
    SearchView searchView ;
    CardView cardView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabcategory_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(ViewAll_Activity.this);
        sd = new SessionManagment(ViewAll_Activity.this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        searchView = (SearchView)findViewById(R.id.seachview);
        cardView = (CardView) findViewById(R.id.card);

        Intent in = getIntent();
        subCategory =  in.getStringExtra("subCategory");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subCategory);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Your Product Here");

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(ViewAll_Activity.this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        retro(subCategory);

    }

    private void retro(String name) {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(ViewAll_Activity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<LatestProductModel> call = apiInterface.getViewAll(name,sd.getLatitude(),sd.getLongitude());
                call.enqueue(new Callback<LatestProductModel>() {
                    @Override
                    public void onResponse(Call<LatestProductModel> call, retrofit2.Response<LatestProductModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        LatestProductModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            cardView.setVisibility(View.GONE);
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                           viewAllAdapter = new LatestProductAdapter(pro.productlist,ViewAll_Activity.this);
                           recyclerView.setAdapter(viewAllAdapter);
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        try {
            viewAllAdapter.filter(text);
        }catch (Exception e){

        }
        return false;
    }
}
