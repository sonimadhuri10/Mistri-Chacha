package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.first.mistrichacha_application.Adapter.CommentAdapter;
import com.first.mistrichacha_application.Adapter.ReviewAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Model.ReviewModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    private ShimmerFrameLayout mShimmerViewContainer;
    ConnectionDetector cd;
    APIInterface apiInterface ;
    String id =  "",name= "";
    ReviewAdapter viewAllAdapter;
    CommentAdapter commentAdapter ;
    LinearLayout llMain ;
    ArrayList<ReviewModel.commentlist> alcomment ;
    ArrayList<ReviewModel.reviewlist> alreview ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        cd = new ConnectionDetector(CommentsActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);

        alcomment = new ArrayList<>();
        alreview = new ArrayList<>();

        Intent in = getIntent();
        id =  in.getStringExtra("id");
        name =  in.getStringExtra("name");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentsActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(name.equals("Reviews")){
            retro(id);
        }else{
           retroCommwent(id);
        }

    }

    private void retro(String id) {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(CommentsActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<ReviewModel> call = apiInterface.getAllReview(id);
                call.enqueue(new Callback<ReviewModel>() {
                    @Override
                    public void onResponse(Call<ReviewModel> call, retrofit2.Response<ReviewModel> response) {
                        alreview.clear();
                        alcomment.clear();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        ReviewModel pro = response.body();

                        if (pro.r_list.size() == 0) {
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            alreview.addAll(pro.r_list);
                            viewAllAdapter = new ReviewAdapter(alreview,CommentsActivity.this);
                            recyclerView.setAdapter(viewAllAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewModel> call, Throwable t) {
                        call.cancel();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });
            }
        }catch (Exception e){

        }


    }

    private void retroCommwent(String id) {

        try {
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(CommentsActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<ReviewModel> call = apiInterface.getAllComments(id);
                call.enqueue(new Callback<ReviewModel>() {
                    @Override
                    public void onResponse(Call<ReviewModel> call, retrofit2.Response<ReviewModel> response) {
                        alreview.clear();
                        alcomment.clear();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        ReviewModel pro = response.body();

                        if (pro.c_list.size() == 0) {
                            llMain.setBackgroundResource(R.drawable.nodatafull);
                        } else {
                            alcomment.addAll(pro.c_list);
                            commentAdapter = new CommentAdapter(alcomment,CommentsActivity.this);
                            recyclerView.setAdapter(commentAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewModel> call, Throwable t) {
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
}
