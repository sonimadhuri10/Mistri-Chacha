package com.first.mistrichacha_application.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    static EditText et1, et2, et3, et4, et5,et6;
    ImageView imgOtp;
    TextView tvResend;
    static String e1 = "", e2 = "", e3 = "", e4 = "", e5 = "",e6="", finalstring = "",
            msg = "", one = "", two = "", three = "", four = "", five = "",six="";
    static ProgressDialog pd;
    ConnectionDetector cd;
    static APIInterface apiInterface;
    SessionManagment sd;
    String device_id="",otp="",mobile="",userid="",apikey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        et6 = (EditText) findViewById(R.id.et6);

        imgOtp = (ImageView) findViewById(R.id.imgMove);
        tvResend = (TextView) findViewById(R.id.tvResend);

        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(OtpActivity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
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

        tvResend.setOnClickListener(this);
        imgOtp.setOnClickListener(this);

        device_id = sd.getFCM();
        Intent in = getIntent();
        otp =   in.getStringExtra("otp");
        mobile =   in.getStringExtra("mobile");
        userid =   in.getStringExtra("userid");
        apikey =   in.getStringExtra("apikey");

    }

    public static void recivedSms(String message, String dev , final Context context) {

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
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e2.equals("")) {
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e3.equals("")) {
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e4.equals("")) {
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else if (e5.equals("")) {
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                }  else if (e6.equals("")) {
                    Toast.makeText(OtpActivity.this, "Fill Complete OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if(finalstring.equals(otp)){
                        Toast.makeText(OtpActivity.this, "Otp Verify Successfully", Toast.LENGTH_SHORT).show();

                        sd.setLOGIN_STATUS("true");
                        sd.setUSER_ID(userid);
                        sd.setKEY_APITOKEN(apikey);
                        Intent in = new Intent(OtpActivity.this, DrawerActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }else{
                        Toast.makeText(OtpActivity.this, "Your otp Does Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tvResend:
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                resendotp(mobile);
                break;
        }
    }

    public void resendotp(String mobile) {
        try{
            pd.show();
            Call<SignupModel> call = apiInterface.getOtpVerify(mobile,sd.getFCM());
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equals("Send otp!")) {
                        otp =  resource.otp;
                        userid=resource.userdata.get(0).id;
                        Toast.makeText(OtpActivity.this, "Otp Resend Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OtpActivity.this, "Wrong Otp..!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(OtpActivity.this, "Please Check your internet connection..!", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }catch (Exception e){

        }


    }

}

