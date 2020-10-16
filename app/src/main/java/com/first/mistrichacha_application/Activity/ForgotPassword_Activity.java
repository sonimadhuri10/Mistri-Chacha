package com.first.mistrichacha_application.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPassword_Activity extends AppCompatActivity implements View.OnClickListener {

    static EditText et1, et2, et3, et4, et5,et6;
    ImageView imgOtp;
    static String e1 = "", e2 = "", e3 = "", e4 = "", e5 = "",e6="", finalstring = "",
            msg = "", one = "", two = "", three = "", four = "", five = "",six="";
    static ProgressDialog pd;
    ConnectionDetector cd;
    static APIInterface apiInterface;
    SessionManagment sd;
    LinearLayout llotp , llReset ;
    String otp="";
    Button btnReset ;
    String password = "",confirmpassword = "" ,token = "";
    EditText etpassword , etConfirmPassword;
    Animation slideUp ;
    TextView tvHeading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        et6 = (EditText) findViewById(R.id.et6);

        imgOtp = (ImageView) findViewById(R.id.imgMove);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        etpassword=(EditText)findViewById(R.id.etNewPassword);
        etConfirmPassword=(EditText)findViewById(R.id.etConfirmpassword);
        btnReset=(Button)findViewById(R.id.btnResetpassword);
        llotp=(LinearLayout)findViewById(R.id.llotp);
        llReset=(LinearLayout)findViewById(R.id.llReset);

        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        slideUp =    AnimationUtils.loadAnimation(this, R.anim.slide_up_dialog);

        tvHeading.setText("Please Enter OTP");

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ForgotPassword_Activity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
        }

        et1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et1.getText().toString().length() == 1) {
                    et2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et2.getText().toString().length() == 1) {
                    et3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et3.getText().toString().length() == 1) {
                    et4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et4.getText().toString().length() == 1) {
                    et5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et5.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et5.getText().toString().length() == 1) {
                    et6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        imgOtp.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        Intent in = getIntent();
        otp =   in.getStringExtra("otp");
        token =   in.getStringExtra("header");

    }

    public static void recivedSms(String message) {

        msg = message.substring(49, 55);
        one = msg.substring(0, 1);
        two = msg.substring(1, 2);
        three = msg.substring(2, 3);
        four = msg.substring(3, 4);
        five = msg.substring(4, 5);
        six = msg.substring(5, 6);

        et1.setText(one);
        et2.setText(two);
        et3.setText(three);
        et4.setText(four);
        et5.setText(five);
        et6.setText(six);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgMove:
                e1 = et1.getText().toString().trim();
                e2 = et2.getText().toString().trim();
                e3 = et3.getText().toString().trim();
                e4 = et4.getText().toString().trim();
                e5 = et5.getText().toString().trim();
                e6 = et6.getText().toString().trim();

                finalstring = e1 + e2 + e3 + e4 + e5 + e6;
                if (e1.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e2.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e3.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e4.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e5.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                }  else if (e6.equals("")) {
                    Toast.makeText(ForgotPassword_Activity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if(finalstring.equals(otp)){
                     llotp.setVisibility(View.GONE);
                     llReset.startAnimation(slideUp);
                     llReset.setVisibility(View.VISIBLE);
                     tvHeading.setText("Reset Your Password");

                    }else{
                        Toast.makeText(ForgotPassword_Activity.this, "Your otp Does Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnResetpassword:

                password = etpassword.getText().toString().trim();
                confirmpassword =  etConfirmPassword.getText().toString().trim() ;

                if(password.equals("")){
                    etpassword.setError("Please Enter New Password");
                }else if(confirmpassword.equals("")){
                    etConfirmPassword.setError("Please Enter Confirm Password");
                }else if(!password.equals(confirmpassword)){
                    etConfirmPassword.setError("Your Password Does Not Match");
                }else{
                  verifyNumber(password);
                }

                break;
        }
    }

    public void verifyNumber(final String mobile) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getForgetPassword("Bearer "+token,mobile);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("Password Updated!")) {
                        Toast.makeText(ForgotPassword_Activity.this, "Your Password Has Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(ForgotPassword_Activity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(ForgotPassword_Activity.this, "Sorry , This No Is Not Register With Us", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(ForgotPassword_Activity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }



}

