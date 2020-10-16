package com.first.mistrichacha_application.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    String name , email="" ,mobile="" , zip="" , city="" , state="" ,
            country="" , photo="",address="",buildin="",flat="" ,type="";
    EditText etName , etMobile , etEmail  , etAddress ,
            etCity , etZip ,etState , etCounty , etBuilding , etFlat ;
    Button btnUpdateProfile ;
    String EmialPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+com+";
    ProgressDialog pd ;
    CircleImageView updateImage ;
    String encImage = "";
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PROFILE_UPDATE_REQUEST = 80;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);

        etName=(EditText) findViewById(R.id.etName);
        etMobile=(EditText) findViewById(R.id.etMobile);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etAddress=(EditText) findViewById(R.id.etAddress);
        etCity=(EditText) findViewById(R.id.etCity);
        etZip=(EditText) findViewById(R.id.etZip);
        etState=(EditText) findViewById(R.id.etState);
        etCounty=(EditText) findViewById(R.id.etCountry);
        etBuilding=(EditText) findViewById(R.id.etBuilding);
        etFlat=(EditText) findViewById(R.id.etFlatNo);
        btnUpdateProfile=(Button)findViewById(R.id.btnUpdate);
        updateImage=(CircleImageView)findViewById(R.id.updateimage);

        Intent in = getIntent();
        name =  in.getStringExtra("name");
        email =  in.getStringExtra("email");
        mobile =  in.getStringExtra("mobile");
        zip =  in.getStringExtra("zip");
        city =  in.getStringExtra("city");
        state =  in.getStringExtra("state");
        country =  in.getStringExtra("country");
        photo =  in.getStringExtra("photo");
        address =  in.getStringExtra("address");
        buildin =  in.getStringExtra("building");
        flat =  in.getStringExtra("flat_no");
        type =  in.getStringExtra("type");

        etName.setText(name);
        etEmail.setText(email);
        etMobile.setText(mobile);
        etAddress.setText(address);
        etCity.setText(city);
        etZip.setText(zip);
        etState.setText(state);
        etCity.setText(city);
        etFlat.setText(flat);
        etBuilding.setText(buildin);

        if(!photo.equals("")){
            Picasso.with(EditProfile.this).load(photo).into(updateImage);
        }

        btnUpdateProfile.setOnClickListener(this);
        updateImage.setOnClickListener(this);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(type.equals("activity")){
            Intent returnIntent = getIntent();
            returnIntent.putExtra("type",type);
            setResult(PROFILE_UPDATE_REQUEST,returnIntent);
            finish();
            super.onBackPressed();
        }else{
            finish();
        }
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.btnUpdate:
            name = etName.getText().toString().trim();
            mobile=etMobile.getText().toString().trim();
            email=etEmail.getText().toString().trim();
            zip=etZip.getText().toString().trim();
            city=etCity.getText().toString().trim();
            state=etState.getText().toString().trim();
            country=etCounty.getText().toString().trim();
            address=etAddress.getText().toString().trim();
            buildin=etBuilding.getText().toString().trim();
            flat=etFlat.getText().toString().trim();

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
            } else if(country.equals("")){
                etCounty.setError("Please Enter Country Name");
            }else if(state.equals("")){
                etState.setError("Please Enter State Name");
            }else if(city.equals("")){
                etCity.setError("Please Enter City Name");
            }else if(zip.equals("")){
                etZip.setError("Please Enter Zipcode");
            } else if (address.equals("")) {
                etAddress.setError("Please Enter Address");
            } else if (flat.equals("")) {
                etFlat.setError("Please Enter Flat No");
            } else if (buildin.equals("")) {
                etBuilding.setError("Please Enter Building Name");
            } else {

                 updateProfile(name, mobile, email,address,city,zip,state,country,encImage,flat,buildin);
            }
            break;
        case R.id.updateimage:
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
            break;
        default:
            break;
    }
    }


    public void updateProfile(String name, String mob, String ema, String address,
                              String city, String zip , String state , final String country,
                              String photo , String flat , String buildng ) {
        try {

            RequestBody requestBody ;
            MultipartBody.Part fileToUpload ;
            RequestBody filename ;

            if(photo.equals("")){
               fileToUpload = null;
               filename = null ;
            }else{
                File file = new File(photo);
                requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
                filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            }

            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody usermob = RequestBody.create(MediaType.parse("text/plain"), mob);
            RequestBody useremail = RequestBody.create(MediaType.parse("text/plain"), ema);
            RequestBody useraddress = RequestBody.create(MediaType.parse("text/plain"), address);
            RequestBody usercity = RequestBody.create(MediaType.parse("text/plain"), city);
            RequestBody userzip = RequestBody.create(MediaType.parse("text/plain"), zip);
            RequestBody userstate = RequestBody.create(MediaType.parse("text/plain"), state);
            RequestBody usercountry = RequestBody.create(MediaType.parse("text/plain"), country);
            RequestBody userflat = RequestBody.create(MediaType.parse("text/plain"), flat);
            RequestBody userbuildung = RequestBody.create(MediaType.parse("text/plain"), buildng);

            pd.show();
            Call<SignupModel> call = apiInterface.getUpdateProfile("Bearer "+sd.getKEY_APITOKEN(),fileToUpload,filename,username,usermob,useremail,
                    useraddress,usercity,userzip,userstate,usercountry,userflat,userbuildung);
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {
                    SignupModel resource = response.body();
                    pd.dismiss();

                   if (resource.success.equalsIgnoreCase("Profile Updated!")) {
                        Toast.makeText(EditProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                        sd.setSETNAME(resource.userdata.get(0).name);
                        sd.setSETEMAIL(resource.userdata.get(0).email);
                        sd.setPHOTO(resource.userdata.get(0).photo);
                        sd.setMobile(resource.userdata.get(0).phone);

                        etName.setText(resource.userdata.get(0).name);
                        etMobile.setText(resource.userdata.get(0).phone);
                        etEmail.setText(resource.userdata.get(0).email);
                        etZip.setText(resource.userdata.get(0).zip);
                        etCity.setText(resource.userdata.get(0).city);
                        etAddress.setText(resource.userdata.get(0).address);
                        etState.setText(resource.userdata.get(0).state);
                        etCounty.setText(resource.userdata.get(0).country);
                        etBuilding.setText(resource.userdata.get(0).building);
                        etFlat.setText(resource.userdata.get(0).flat_no);

                   } else if (resource.success.equalsIgnoreCase("This Email is already Used")) {
                        Toast.makeText(EditProfile.this, "Your mobile No or email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(EditProfile.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });

        }catch (Exception e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if  (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                encImage = picturePath ;
                cursor.close();

                updateImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

        } catch (Exception e) {
            Toast.makeText(EditProfile.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
