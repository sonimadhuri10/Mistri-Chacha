package com.first.mistrichacha_application.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.MainActivity;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etpassword ,etMobile,etForgotNo;
    TextView tvForgot , tvSkipAndView;
    String email ="",password="",mobile="",forgotNo ="";
    Button btnlogin , btnLoginViaOtp, btnGetOtp , btnVerify;
    LinearLayout llSignup , llLoginNormal ,llLoginOtp ,llForgot ;
    SessionManagment sd;
    ConnectionDetector cd;
    APIInterface apiInterface;
    ProgressDialog pd ;
    String EmialPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+com+";
    Animation slideUp , fadein;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etEmail=(EditText)findViewById(R.id.etEmail);
        etpassword=(EditText)findViewById(R.id.etPassword);
        etMobile=(EditText)findViewById(R.id.etMobile);
        etForgotNo=(EditText)findViewById(R.id.etforgotMobile);
        btnlogin=(Button)findViewById(R.id.btnLogin);
        btnLoginViaOtp=(Button)findViewById(R.id.btnLoginViaOTp);
        btnGetOtp=(Button)findViewById(R.id.btnGetOtp);
        btnVerify=(Button)findViewById(R.id.btnVerifyNo);
        llSignup=(LinearLayout)findViewById(R.id.llSignup);
        llLoginNormal=(LinearLayout)findViewById(R.id.llLoginNormal);
        llLoginOtp=(LinearLayout)findViewById(R.id.llLoginOtp);
        llForgot=(LinearLayout)findViewById(R.id.llForgot);
        tvForgot=(TextView)findViewById(R.id.tvForgotPassword);
        tvSkipAndView=(TextView)findViewById(R.id.tvSkipandView);

        sd = new SessionManagment(this);
        cd = new ConnectionDetector(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);

        slideUp =    AnimationUtils.loadAnimation(this, R.anim.slide_up_dialog);
        fadein =    AnimationUtils.loadAnimation(this, R.anim.fadein);
        permission();

        llSignup.setOnClickListener(this);
        llSignup.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        btnLoginViaOtp.setOnClickListener(this);
        btnGetOtp.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        tvSkipAndView.setOnClickListener(this);

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(LoginActivity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.llSignup:
                 Intent in = new Intent(LoginActivity.this,SignupActivity.class);
                 startActivity(in);
                 overridePendingTransition(R.anim.left_in, R.anim.left_out);
                 break;
             case R.id.btnLogin:
                 email=etEmail.getText().toString().trim();
                 password = etpassword.getText().toString().trim();

             if (email.equals("")) {
                 etEmail.setError("Please Enter email address");
             } else if (!email.matches(EmialPattern)) {
                 etEmail.setError("Please Enter valid Email Address");
             } else if(password.equals("")){
                 etpassword.setError("Please Enter Password");
             }else{
              login(email,password,sd.getFCM());
             }
                 break;
             case R.id.btnLoginViaOTp:

                 llLoginNormal.setVisibility(View.GONE);
                 llLoginOtp.startAnimation(slideUp);
                 llLoginOtp.setVisibility(View.VISIBLE);

                 break;
             case R.id.btnGetOtp:
                 mobile=etMobile.getText().toString().trim();

                 if (mobile.equals("")) {
                     etMobile.setError("Please Enter Mobile No");
                 } else if(mobile.length()!= 10) {
                     etMobile.setError("Please Enter 10 digit Mobile No");
                 }else{
                     otp(mobile);
                 }
                 break;
              case R.id.tvForgotPassword:
                  llLoginNormal.setVisibility(View.GONE);
                  llForgot.startAnimation(slideUp);
                  llForgot.setVisibility(View.VISIBLE);
                 break;
             case R.id.btnVerifyNo:
              forgotNo = etForgotNo.getText().toString().trim();
                 if (forgotNo.equals("")) {
                     etForgotNo.setError("Please Enter Mobile No");
                 } else if(forgotNo.length()!= 10) {
                     etForgotNo.setError("Please Enter 10 digit Mobile No");
                 }else{
                     verifyNumber(forgotNo);
                 }
                 break;
             case R.id.tvSkipandView:
                 sd.setLOGIN_STATUS("skip");
                /* sd.setLatitude("latitude");
                 sd.setLocation("location");*/
                 Intent in1 = new Intent(LoginActivity.this,DrawerActivity.class);
                 startActivity(in1);
                 finish();
                 overridePendingTransition(R.anim.left_in, R.anim.left_out);

                 break;
             default:
                 break;
         }
    }

    public void otp(final String mobile) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getOtpVerify(mobile,sd.getFCM());
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("Send otp!")) {
                        Toast.makeText(LoginActivity.this, "Send Otp Successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginActivity.this, OtpActivity.class);
                        in.putExtra("mobile",mobile);
                        in.putExtra("otp",resource.otp);
                        in.putExtra("userid",resource.userdata.get(0).id);
                        in.putExtra("apikey",resource.userdata.get(0).api_token);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(LoginActivity.this, "Sorry , This No Is Not Register With Us", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

    public void login(String email, String password, String dev) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getLogin(email, password, dev);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equalsIgnoreCase("Login Success!")) {
                        Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();

                        sd.setLOGIN_STATUS("true");
                        sd.setUSER_ID(resource.userdata.get(0).id);
                        sd.setKEY_APITOKEN(resource.userdata.get(0).api_token);
                        sd.setSETNAME(resource.userdata.get(0).name);
                        sd.setSETEMAIL(resource.userdata.get(0).email);
                        sd.setMobile(resource.userdata.get(0).phone);
                        sd.setPHOTO(resource.userdata.get(0).photo);

                        Intent in1 = new Intent(LoginActivity.this,DrawerActivity.class);
                        startActivity(in1);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    } else if (resource.success.equalsIgnoreCase("Credentials Doesn't Match !")) {
                        Toast.makeText(LoginActivity.this, "Wrong Email Address & Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

    public void verifyNumber(final String mobile) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getVerifyNumber(mobile);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();

                    if (resource.success.equals("Send otp!")) {
                        Toast.makeText(LoginActivity.this, "Verify Successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginActivity.this, ForgotPassword_Activity.class);
                        in.putExtra("otp",resource.otp);
                        in.putExtra("header",resource.userdata.get(0).api_token);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(LoginActivity.this, "Sorry , This No Is Not Register With Us", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

    public void permission() {
        if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                        },
                        3);
            } else {
            }
        }
    }

}
