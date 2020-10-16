package com.first.mistrichacha_application.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.first.mistrichacha_application.Adapter.CommentAdapter;
import com.first.mistrichacha_application.Adapter.ReviewAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.PaymentModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class GiveFeedBAckActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    APIInterface apiInterface ;
    SessionManagment sd;
    ProgressDialog pd ;
    String id = "",name;
    RatingBar ratingBar ;
    EditText etText ;
    Button btnSubmit ;
    String rating = "" , review="";
    LinearLayout llView ;
    TextView tvHeading ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);

        cd = new ConnectionDetector(GiveFeedBAckActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sd = new SessionManagment(GiveFeedBAckActivity.this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait..");
        ratingBar =(RatingBar)findViewById(R.id.rating);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        etText=(EditText) findViewById(R.id.etReview);
        llView=(LinearLayout)findViewById(R.id.llView);
        tvHeading=(TextView)findViewById(R.id.tvHeading);

        Intent in = getIntent();
        id = in.getStringExtra("id");
        name =  in.getStringExtra("name");



        if(name.equals("Comments")){
            llView.setVisibility(View.GONE);
            tvHeading.setText("Comments");
        }else{
            llView.setVisibility(View.VISIBLE);
            tvHeading.setText("Reviews");
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");


       btnSubmit.setOnClickListener(this);
    }

    public void review(String pro_id , String rating , String review) {
        try{
            pd.show();
            Call<PaymentModel> call = apiInterface.giveReview("Bearer "+sd.getKEY_APITOKEN(),pro_id,rating,review);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();


                    if (resource.success.equals("Buy This Product First")) {
                        Toast.makeText(GiveFeedBAckActivity.this, "Sorry , Firstly You Have To Buy This Product", Toast.LENGTH_SHORT).show();
                    }else if(resource.success.equals("You Have Reviewed Already.")){
                        Toast.makeText(GiveFeedBAckActivity.this, "You Have Reviewed Already This Product", Toast.LENGTH_SHORT).show();
                    }else if(resource.success.equals("Your Rating Submitted Successfully.")){
                        Intent in111= new Intent(GiveFeedBAckActivity.this,CommentsActivity.class);
                        in111.putExtra("id",id);
                        in111.putExtra("name","Reviews");
                        startActivity(in111);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(GiveFeedBAckActivity.this, "Sorry , Try after some time", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(GiveFeedBAckActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

    public void review_comment(final String id , String comment) {
        try{
            pd.show();
            Call<PaymentModel> call = apiInterface.giveComment("Bearer "+sd.getKEY_APITOKEN(),id, comment);
            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(Call<PaymentModel> call, retrofit2.Response<PaymentModel> response) {
                    PaymentModel resource = response.body();
                    pd.dismiss();

                    if (resource.success.equals("comment added successfully")) {
                        Toast.makeText(GiveFeedBAckActivity.this, "Submit Successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(GiveFeedBAckActivity.this, CommentsActivity.class);
                        in.putExtra("id",id);
                        in.putExtra("name","Comments");
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(GiveFeedBAckActivity.this, "Sorry , Try after some time", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PaymentModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(GiveFeedBAckActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }


    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.btnSubmit:
                 if(name.equals("Comments")){
                     review = etText.getText().toString().trim();
                     if(review.equals("")){
                         etText.setError("Please Write Your Comment");
                     }else{
                         review_comment(id,review);
                     }
                 }else{
                     review = etText.getText().toString().trim();
                     rating = String.valueOf(ratingBar.getRating());

                     if(review.equals("")){
                         etText.setError("Please Write Your Review");
                     }else{
                         review(id,rating,etText.getText().toString());
                     }
                 }

             break;
         default:
             break;
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
