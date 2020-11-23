package com.first.mistrichacha_application.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener ,  GoogleApiClient.OnConnectionFailedListener  {

    LinearLayout llLogin ,llGoogleSignup ;
    EditText etName , etMobile , etEmail , etPassword , etConfirmPassworsd , etAddress ,
             etCity , etZip ,etState , etCounty ;
    Button btnSignup ;
    String name = "",email = "",mobile="",password="",
            confirmpassword="",address="",city="",state="",zipcode="",country="",device_id;
    SessionManagment sd;
    ConnectionDetector cd;
    ProgressDialog pd;
    APIInterface apiInterface ;
    String EmialPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+com+";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_layout);

        llLogin=(LinearLayout)findViewById(R.id.llLogin);
        llGoogleSignup=(LinearLayout)findViewById(R.id.llGoogleSignup);
        btnSignup=(Button) findViewById(R.id.btnSignup);
        etName=(EditText) findViewById(R.id.etName);
        etMobile=(EditText) findViewById(R.id.etMobile);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etPassword=(EditText) findViewById(R.id.etpassword);
        etConfirmPassworsd=(EditText) findViewById(R.id.etConfirmpassword);
        etAddress=(EditText) findViewById(R.id.etAddress);
        etCity=(EditText) findViewById(R.id.etCity);
        etZip=(EditText) findViewById(R.id.etZip);
        etState=(EditText) findViewById(R.id.etState);
        etCounty=(EditText) findViewById(R.id.etCountry);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait....");

        llLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        llGoogleSignup.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SignupActivity.this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.llLogin:
            Intent in = new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(in);
            break;
        case R.id.btnSignup:
            name = etName.getText().toString().trim();
            mobile = etMobile.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            confirmpassword = etConfirmPassworsd.getText().toString().trim();
            address = etAddress.getText().toString().trim();
            city = etCity.getText().toString().trim();
            zipcode = etZip.getText().toString().trim();
            state = etState.getText().toString().trim();
            country = etCounty.getText().toString().trim();

            if (name.equals("")) {
                etName.setError("Please Enter Name");
            } else if (mobile.equals("")) {
                etMobile.setError("Please Enter Mobile No");
            } else if (mobile.length() != 10) {
                etMobile.setError("Please Enter Valid Mobile No");
            } else if (email.equals("")) {
                etEmail.setError("Please Enter email address");
            } else if (!email.matches(EmialPattern)) {
                etEmail.setError("Please Enter valid Email Address");
            } else if (password.equals("")) {
                etPassword.setError("Please Enter Password");
            }else if (confirmpassword.equals("")) {
                etConfirmPassworsd.setError("Please Enter Confirm Password");
            }else if (!password.equals(confirmpassword)) {
                etConfirmPassworsd.setError("Your Password Does Not Match");
            }else if(country.equals("")){
                etCounty.setError("Please Enter Country Name");
            }else if(state.equals("")){
                etState.setError("Please Enter State Name");
            }else if(city.equals("")){
                etCity.setError("Please Enter City Name");
            }else if(zipcode.equals("")){
                etZip.setError("Please Enter Zipcode");
            } else if (address.equals("")) {
                etAddress.setError("Please Enter Address");
            } else {
                signin(name, mobile, email, password, confirmpassword,address,city,zipcode,state,country,device_id);
            }

            break;
        case R.id.llGoogleSignup:
            signIn();
            break;
        default:

            break;
    }
    }

    public void signin(String name, String mob, String ema, String pass, String confirm_pass,String address,
                       String city,String zip , String state , String country , String device_id) {
        try {
            pd.show();
            Call<SignupModel> call = apiInterface.getRegister(name, mob, ema, pass, confirm_pass,address,city,zip,state,country,device_id,"");
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();
                    if (resource.success.equalsIgnoreCase("Profile Created Successful!")) {
                        Toast.makeText(SignupActivity.this, "Signin Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    } else if(resource.success.equalsIgnoreCase("Profile Created From Google")){
                        Toast.makeText(SignupActivity.this, "Signin Successfully, Please Check Your Register Mail To Get The Password", Toast.LENGTH_SHORT).show();
                        signOut();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else if (resource.success.equalsIgnoreCase("This Email is already Used")) {
                        Toast.makeText(SignupActivity.this, "Your mobile No or email already exists", Toast.LENGTH_SHORT).show();
                        signOut();
                    } else {
                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                   }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(SignupActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });

        }catch (Exception e){

        }
    }

    @SuppressLint("StringFormatInvalid")
    private void handleSignInResult(GoogleSignInResult result) {
        //  Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            updateUI(true);
        } else {
            updateUI(false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
           // Toast.makeText(SignupActivity.this, String.valueOf(statusCode)+"madhu", Toast.LENGTH_SHORT).show();
            handleSignInResult(result);
        }
    }

    // [START signIn]
    public void signIn() {
        pd.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
          }
      });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signin(name,"",email,"","","","","","","",sd.getFCM());
        }else{
            pd.dismiss();
        }
    }
}


       /*
       
*/