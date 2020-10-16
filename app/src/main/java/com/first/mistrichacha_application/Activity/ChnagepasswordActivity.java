package com.first.mistrichacha_application.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Adapter.WishListAdapter;
import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class ChnagepasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    APIInterface apiInterface ;
    ProgressDialog pd ;
    SessionManagment sd ;
    EditText etPassword , etNewPassword ,etConfirmPassword ;
    Button btnChnagePassord ;
    String password = "", new_password="", confirm_password="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chnagepassword_layout);


        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");

        apiInterface = APIClient.getClient().create(APIInterface.class);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        etPassword=(EditText)findViewById(R.id.etOldPassword);
        etNewPassword=(EditText)findViewById(R.id.etNewPassword);
        etConfirmPassword=(EditText)findViewById(R.id.etConfirmPassword);
        btnChnagePassord=(Button)findViewById(R.id.btnChnagePassword);

        btnChnagePassord.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChnagePassword:
                password = etPassword.getText().toString().trim();
                new_password = etNewPassword.getText().toString().trim();
                confirm_password = etConfirmPassword.getText().toString().trim();

                if(password.equals("")){
                    etPassword.setError("Please Enter Current Password");
                }else if(new_password.equals("")){
                    etNewPassword.setError("Please Enter New Password");
                }else if(confirm_password.equals("")){
                    etConfirmPassword.setError("Please Enter Confirm Password");
                }else if (!new_password.equals(confirm_password)){
                    etConfirmPassword.setError("Your Password Does Not Match");
                }else{
                    chnagepassword(new_password,password);
                }
                break;
            default:

                break;
        }
    }

    public void chnagepassword(String newpassword , String oldPassword) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getChnagePassword("Bearer "+sd.getKEY_APITOKEN(),newpassword,oldPassword);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("Password updated")) {
                        Toast.makeText(ChnagepasswordActivity.this, "Your Password Is Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else if(resource.success.equals("Invalid Old Password")){
                        Toast.makeText(ChnagepasswordActivity.this, "Your Current Password Is Wrong", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChnagepasswordActivity.this, "Try After Some Time", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(ChnagepasswordActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
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
