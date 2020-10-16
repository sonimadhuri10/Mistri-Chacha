package com.first.mistrichacha_application.Activity;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.MyOrderAdapter;
import com.first.mistrichacha_application.Adapter.OrderDetailAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDetailActivity  extends AppCompatActivity {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    OrderDetailAdapter subCategoryAdapter;
    LinearLayout llMain ;
    CardView cardView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabcategory_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(OrderDetailActivity.this);
        sd= new SessionManagment(OrderDetailActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);

        cardView =(CardView)findViewById(R.id.card);

        cardView.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order's Detail");

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent in = getIntent();
        String id = in.getStringExtra("id");

        retro(id);

    }

    private void retro(final String id) {
        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(OrderDetailActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<PaymentModel> call = apiInterface.getMyOrderDetails("Bearer "+sd.getKEY_APITOKEN(),id);
                call.enqueue(new Callback<PaymentModel>() {
                    @Override
                    public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        PaymentModel pro = response.body();

                        if (pro.productlist.size() == 0) {
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            subCategoryAdapter = new OrderDetailAdapter(pro.productlist,OrderDetailActivity.this , id);
                            recyclerView.setAdapter(subCategoryAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<PaymentModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);

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
}
