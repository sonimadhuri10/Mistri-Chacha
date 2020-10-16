package com.first.mistrichacha_application.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SessionManagment sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sd = new SessionManagment(this);

        new Handler().postDelayed(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void run() {
                 if (sd.getLOGIN_STATUS().equals("true")) {
                    Intent i = new Intent(getApplicationContext(), DrawerActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }else if(sd.getLOGIN_STATUS().equals("skip")){
                     Intent i = new Intent(getApplicationContext(), DrawerActivity.class);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.left_in, R.anim.left_out);
                 } else {
                    Intent i = new Intent(getApplicationContext(), SliderActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

